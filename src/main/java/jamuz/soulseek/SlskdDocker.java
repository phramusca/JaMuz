/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jamuz.soulseek;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.HealthState;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import jamuz.utils.Popup;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

//FIXME ! Check that docker is installed and if not => https://docs.docker.com/engine/install/ or https://rancherdesktop.io/

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class SlskdDocker {

//    https://github.com/slskd/slskd/tree/master
//    https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md
//    https://www.baeldung.com/docker-java-api
    private static final String CONTAINER_NAME = "jamuz-slskd";
    private final boolean SLSKD_SWAGGER;
    private final boolean SLSKD_NO_AUTH;
    private final String SLSKD_SLSK_USERNAME;
    private final String SLSKD_SLSK_PASSWORD;
    private final String serverPath;
    private final String musicPath;
    private final DockerClient dockerClient;
    private final boolean reCreate;

    /**
     *
     * @param SLSKD_SLSK_USERNAME
     * @param SLSKD_SLSK_PASSWORD
     * @param serverPath
     * @param musicPath
     * @param SLSKD_SWAGGER
     * @param SLSKD_NO_AUTH
     * @param reCreate
     */
    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean SLSKD_SWAGGER, boolean SLSKD_NO_AUTH, boolean reCreate) {
        this(SLSKD_SLSK_USERNAME, SLSKD_SLSK_PASSWORD, serverPath, musicPath, SLSKD_SWAGGER, SLSKD_NO_AUTH, reCreate, DockerClientBuilder.getInstance().build());
    }

    /**
     *
     * @param SLSKD_SLSK_USERNAME
     * @param SLSKD_SLSK_PASSWORD
     * @param serverPath
     * @param musicPath
     * @param reCreate
     */
    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean reCreate) {
        this(SLSKD_SLSK_USERNAME, SLSKD_SLSK_PASSWORD, serverPath, musicPath, false, true, reCreate);
    }

    /**
     *
     * @param SLSKD_SLSK_USERNAME
     * @param SLSKD_SLSK_PASSWORD
     * @param serverPath
     * @param musicPath
     * @param SLSKD_SWAGGER
     * @param SLSKD_NO_AUTH
     * @param reCreate
     * @param dockerClient
     */
    private SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean SLSKD_SWAGGER, boolean SLSKD_NO_AUTH, boolean reCreate, DockerClient dockerClient) {
        this.SLSKD_SWAGGER = SLSKD_SWAGGER;
        this.SLSKD_NO_AUTH = SLSKD_NO_AUTH;
        this.SLSKD_SLSK_USERNAME = SLSKD_SLSK_USERNAME;
        this.SLSKD_SLSK_PASSWORD = SLSKD_SLSK_PASSWORD;
        this.serverPath = serverPath;
        this.musicPath = musicPath;
        this.dockerClient = dockerClient;
        this.reCreate = reCreate;
    }

    public boolean start() {
        try {
            Container container = getContainer();
            if (container == null) {
                createAndStartContainer();
                return true;
            } else if (reCreate) {
                removeAndStartContainer(container);
                return true;
            } else {
                State state = getState(container);
                switch (state) {
                    case dead:
                        removeAndStartContainer(container);
                        return true;
                    case exited:
                        dockerClient.startContainerCmd(container.getId()).exec();
                        return true;
                    case paused:
                        dockerClient.stopContainerCmd(container.getId()).exec();
                        dockerClient.startContainerCmd(container.getId()).exec();
                        return true;
                    case created:
                    case restarting:
                    case running:
                        return true;
                }
                return false;
            }
        } catch (DockerException ex) {
            Popup.error(ex);
        }
        return false;
    }

    public void stop() {
        Container container = getContainer();
        if (container != null) {
            dockerClient.stopContainerCmd(container.getId()).exec();
        }
    }

    private void removeAndStartContainer(Container container) {
        if (container != null && !getState(container).equals(State.exited)) {
            stop();
            dockerClient.killContainerCmd(container.getId()).exec();
        }
        if (container != null) {
            dockerClient.removeContainerCmd(container.getId()).withForce(true).withRemoveVolumes(true).exec();
        }
        createAndStartContainer();
    }

    //FIXME ! doc `docker pull slskd/slskd`, or better :
//    String imageName = "slskd/slskd";
//        dockerClient.pullImageCmd(imageName)
//                .exec(new PullImageResultCallback())
//                .awaitSuccess();
    private void createAndStartContainer() {
        
        CreateContainerResponse container
                = dockerClient.createContainerCmd("slskd/slskd:latest")
                        .withName(CONTAINER_NAME)
                        .withEnv("SLSKD_REMOTE_CONFIGURATION=true",
                                "SLSKD_REMOTE_FILE_MANAGEMENT=true",
                                "SLSKD_NO_AUTH=" + SLSKD_NO_AUTH,
                                "SLSKD_SLSK_USERNAME=" + SLSKD_SLSK_USERNAME,
                                "SLSKD_SLSK_PASSWORD=" + SLSKD_SLSK_PASSWORD,
                                "SLSKD_SWAGGER=" + SLSKD_SWAGGER,
                                "SLSKD_SHARED_DIR=/music")
                        .withExposedPorts(ExposedPort.tcp(5030), ExposedPort.tcp(5031), ExposedPort.tcp(50300))
                        .withHostConfig(HostConfig.newHostConfig()
                                .withPortBindings(
                                        new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5030), ExposedPort.tcp(5030)),
                                        new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5031), ExposedPort.tcp(5031)),
                                        new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 50300), ExposedPort.tcp(50300))
                                )
                                .withBinds(Bind.parse(serverPath + ":/app"), Bind.parse(musicPath + ":/music"))
                        )
                        .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
    }

    public String checkContainerHealthAndFetchLogs(ResultCallback<Frame> resultCallback) {
        try {
            Container container = getContainer();
            Instant pastTime = Instant.now().minusSeconds(120);
            int sinceTimestamp = (int) pastTime.getEpochSecond();
            dockerClient.logContainerCmd(container.getId())
                    .withFollowStream(true)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withSince(sinceTimestamp)
                    .exec(resultCallback);

            while (true) {
                State state = getState(container);
                switch (state) {
                    case created:
                    case restarting:
                    case running:
                        InspectContainerResponse containerRes = dockerClient.inspectContainerCmd(container.getId()).exec();
                        HealthState health = containerRes.getState().getHealth();
                        if (health != null && "healthy".equalsIgnoreCase(health.getStatus())) {
                            resultCallback.close(); // Close log streaming
                            return "Container is healthy!";
                        }
                        Thread.sleep(1000);
                        break;
                    case dead:
                    case exited:
                    case paused:
                        return "Container is " + state;
                    default:
                        throw new AssertionError();
                }
            }
        } catch (NotFoundException | IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }

    Container getContainer() {
        List<String> names = new ArrayList<>();
        names.add(CONTAINER_NAME);
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowSize(true)
                .withShowAll(true)
                .withNameFilter(names)
                .exec();
        if (!containers.isEmpty()) {
            return containers.get(0);
        }
        return null;
    }

    State getState(Container container) {
        return State.valueOf(container.getState());
    }

    enum State {
        created, // A container that has been created (e.g. with docker create) but not started
        restarting, // A container that is in the process of being restarted
        running, // A currently running container
        paused, // A container whose processes have been paused
        exited, // A container that ran and completed ("stopped" in other contexts, although a created container is technically also "stopped")
        dead, // A container that the daemon tried and failed to stop (usually due to a busy device or resource used by the container) - Added on v1.22
    }
}
