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
import com.github.dockerjava.api.command.PullImageResultCallback;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class SlskdDocker {

//    https://github.com/slskd/slskd/tree/master
//    https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md
//    https://www.baeldung.com/docker-java-api

    /**
     * Official Docker image for slskd; only the tag is configurable ({@code slsk.docker.image.tag}).
     *
     * @see <a href="https://hub.docker.com/r/slskd/slskd">slskd/slskd on Docker Hub</a>
     */
    public static final String DOCKER_IMAGE_REPOSITORY = "slskd/slskd";

    /**
     * Default tag when {@code slsk.docker.image.tag} is missing from {@code Slsk.properties}.
     */
    public static final String DEFAULT_DOCKER_IMAGE_TAG = "0.24.5";

    private static final String CONTAINER_NAME = "jamuz-slskd";
    private final boolean SLSKD_SWAGGER;
    private final boolean SLSKD_NO_AUTH;
    private final String SLSKD_SLSK_USERNAME;
    private final String SLSKD_SLSK_PASSWORD;
    private final String serverPath;
    private final String musicPath;
    private final DockerClient dockerClient;
    private final boolean reCreate;
    private final String dockerImageTag;
    private final String dockerImage;
    private final String sharedExcludeMultiline;

    /**
     * Value for {@code SLSKD_SHARED_DIR}: the {@code /music} share in the container, plus exclusions
     * {@code !/music/...} (absolute paths inside the container), semicolon-separated.
     *
     * @param hostMusicPath host path of the shared folder (mounted at {@code /music})
     * @param excludeMultiline one line per path (relative to the shared root, or absolute under it);
     *                         no {@code !} or {@code -} prefix required (added for slskd); a leading
     *                         {@code !} or {@code -} is accepted and stripped if present
     * @see <a href="https://github.com/slskd/slskd/blob/master/docs/config.md">slskd configuration (shares)</a>
     */
    public static String buildSharedDirEnvValue(String hostMusicPath, String excludeMultiline) {
        List<String> segments = new ArrayList<>();
        segments.add("/music");
        if (excludeMultiline == null || excludeMultiline.isBlank()) {
            return String.join(";", segments);
        }
        for (String line : excludeMultiline.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.startsWith("!") || trimmed.startsWith("-")) {
                trimmed = trimmed.substring(1).trim();
            }
            String containerPath = toContainerExcludedPath(hostMusicPath, trimmed);
            if (containerPath != null && !containerPath.isEmpty()) {
                segments.add("!" + containerPath);
            }
        }
        return String.join(";", segments);
    }

    private static String toContainerExcludedPath(String hostMusicRoot, String userPath) {
        try {
            Path root = Paths.get(hostMusicRoot).normalize().toAbsolutePath();
            Path resolved = Paths.get(userPath).isAbsolute()
                    ? Paths.get(userPath).normalize().toAbsolutePath()
                    : root.resolve(userPath).normalize().toAbsolutePath();
            if (!resolved.startsWith(root)) {
                return null;
            }
            Path rel = root.relativize(resolved);
            if (rel.getNameCount() == 0) {
                return null;
            }
            String relUnix = rel.toString().replace('\\', '/');
            return "/music/" + relUnix;
        } catch (Exception e) {
            return null;
        }
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
     * @param dockerImageTag tag of the Docker image {@value #DOCKER_IMAGE_REPOSITORY} (ex. {@code 0.24.5})
     */
    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean SLSKD_SWAGGER, boolean SLSKD_NO_AUTH, boolean reCreate, String dockerImageTag) {
        this(SLSKD_SLSK_USERNAME, SLSKD_SLSK_PASSWORD, serverPath, musicPath, SLSKD_SWAGGER, SLSKD_NO_AUTH, reCreate, normalizeDockerImageTag(dockerImageTag), null, DockerClientBuilder.getInstance().build());
    }

    /**
     * @param sharedExcludeMultiline lines of subfolders to exclude from the slskd share (see {@link #buildSharedDirEnvValue})
     */
    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean reCreate, String dockerImageTag, String sharedExcludeMultiline) {
        this(SLSKD_SLSK_USERNAME, SLSKD_SLSK_PASSWORD, serverPath, musicPath, false, true, reCreate, normalizeDockerImageTag(dockerImageTag), sharedExcludeMultiline, DockerClientBuilder.getInstance().build());
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
     * @param dockerImageTag normalized tag (non empty)
     */
    private SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean SLSKD_SWAGGER, boolean SLSKD_NO_AUTH, boolean reCreate, String dockerImageTag, String sharedExcludeMultiline, DockerClient dockerClient) {
        this.SLSKD_SWAGGER = SLSKD_SWAGGER;
        this.SLSKD_NO_AUTH = SLSKD_NO_AUTH;
        this.SLSKD_SLSK_USERNAME = SLSKD_SLSK_USERNAME;
        this.SLSKD_SLSK_PASSWORD = SLSKD_SLSK_PASSWORD;
        this.serverPath = serverPath;
        this.musicPath = musicPath;
        this.dockerClient = dockerClient;
        this.reCreate = reCreate;
        this.dockerImageTag = dockerImageTag;
        this.dockerImage = DOCKER_IMAGE_REPOSITORY + ":" + dockerImageTag;
        this.sharedExcludeMultiline = sharedExcludeMultiline;
    }

    private static String normalizeDockerImageTag(String dockerImageTag) {
        if (dockerImageTag == null || dockerImageTag.isBlank()) {
            return DEFAULT_DOCKER_IMAGE_TAG;
        }
        return dockerImageTag.trim();
    }

    private void pullImageIfAbsent() {
        try {
            dockerClient.inspectImageCmd(dockerImage).exec();
        } catch (NotFoundException e) {
            try {
                PullImageResultCallbackWithRethrow callback = new PullImageResultCallbackWithRethrow();
                dockerClient.pullImageCmd(DOCKER_IMAGE_REPOSITORY)
                        .withTag(dockerImageTag)
                        .exec(callback)
                        .awaitCompletion();
                callback.rethrowFirstErrorIfAny();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new DockerException("Interrupted while pulling Docker image " + dockerImage, 0, ie);
            }
        }
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

    private void createAndStartContainer() {
        pullImageIfAbsent();
        CreateContainerResponse container
                = dockerClient.createContainerCmd(dockerImage)
                        .withName(CONTAINER_NAME)
                        .withEnv("SLSKD_REMOTE_CONFIGURATION=true",
                                "SLSKD_REMOTE_FILE_MANAGEMENT=true",
                                "SLSKD_NO_AUTH=" + SLSKD_NO_AUTH,
                                "SLSKD_SLSK_USERNAME=" + SLSKD_SLSK_USERNAME,
                                "SLSKD_SLSK_PASSWORD=" + SLSKD_SLSK_PASSWORD,
                                "SLSKD_SWAGGER=" + SLSKD_SWAGGER,
                                "SLSKD_SHARED_DIR=" + buildSharedDirEnvValue(musicPath, sharedExcludeMultiline))
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

    /**
     * Subclass to call {@code throwFirstError()} (protected in {@link PullImageResultCallback}).
     */
    private static final class PullImageResultCallbackWithRethrow extends PullImageResultCallback {
        void rethrowFirstErrorIfAny() {
            throwFirstError();
        }
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
