package jamuz.soulseek;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.HealthState;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author raph
 */
public class SlskdDocker {
    
//    https://github.com/slskd/slskd/tree/master
//    https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md
//    https://www.baeldung.com/docker-java-api
    
    //FIXME !!! Need to apply new config to running container (if any) if anything below changes in gui (apply button ? or restart ?)
    private final boolean SLSKD_SWAGGER ;
    private final boolean SLSKD_NO_AUTH;
    private final String SLSKD_SLSK_USERNAME;
    private final String SLSKD_SLSK_PASSWORD;
    private final String serverPath;
    private final String musicPath;
    private final DockerClient dockerClient;

    private static final String CONTAINER_NAME = "jamuz-slskd";

    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath, boolean SLSKD_SWAGGER, boolean SLSKD_NO_AUTH) {
        this.SLSKD_SWAGGER = SLSKD_SWAGGER;
        this.SLSKD_NO_AUTH = SLSKD_NO_AUTH;
        this.SLSKD_SLSK_USERNAME = SLSKD_SLSK_USERNAME;
        this.SLSKD_SLSK_PASSWORD = SLSKD_SLSK_PASSWORD;
        this.serverPath = serverPath;
        this.musicPath = musicPath;
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }
    
    public SlskdDocker(String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath, String musicPath) {
        this.SLSKD_SWAGGER = true; //FIXME !!!!!!!!!!!!!!!!! set to false when done
        this.SLSKD_NO_AUTH = true;
        this.SLSKD_SLSK_USERNAME = SLSKD_SLSK_USERNAME;
        this.SLSKD_SLSK_PASSWORD = SLSKD_SLSK_PASSWORD;
        this.serverPath = serverPath;
        this.musicPath = musicPath;
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }
    
    public boolean start() {
        Container container = getContainer();
        if(container == null) {
            createAndStartContainer();
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
    }
    
    public void stop() {
        Container container = getContainer();
        if(container != null) {
            dockerClient.stopContainerCmd(container.getId()).exec();
        }
    }
    
    private void removeAndStartContainer(Container container) {
        stop();
        dockerClient.killContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).withForce(true).withRemoveVolumes(true).exec();
        createAndStartContainer();
    }
    
    private void createAndStartContainer() {
        CreateContainerResponse container
          = dockerClient.createContainerCmd("slskd/slskd:latest")
            .withName(CONTAINER_NAME)
            .withEnv("SLSKD_REMOTE_CONFIGURATION=true",
                    "SLSKD_NO_AUTH="+SLSKD_NO_AUTH,
                    "SLSKD_SLSK_USERNAME="+SLSKD_SLSK_USERNAME,
                    "SLSKD_SLSK_PASSWORD="+SLSKD_SLSK_PASSWORD,
                    "SLSKD_SWAGGER="+SLSKD_SWAGGER,
                    "SLSKD_SHARED_DIR=/music")
            .withExposedPorts(ExposedPort.tcp(5030), ExposedPort.tcp(5031), ExposedPort.tcp(50300))
            .withHostConfig(HostConfig.newHostConfig()
                .withPortBindings(
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5030), ExposedPort.tcp(5030)),
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5031), ExposedPort.tcp(5031)),
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 50300), ExposedPort.tcp(50300))
                )
                .withBinds(Bind.parse(serverPath+":/app"))
                .withBinds(Bind.parse(musicPath+":/music"))
            )
            .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
    }
    
    public String checkContainerHealthAndFetchLogs(JTextArea logTextArea) {
        try {
            Container container = getContainer();
            
            final ResultCallback<Frame> resultCallback = new ResultCallback<Frame>() {
                @Override
                public void onStart(Closeable closeable) {
                    SwingUtilities.invokeLater(() -> {
                        logTextArea.append("Starting ...\n"); 
                    });
                }

                @Override
                public void onNext(com.github.dockerjava.api.model.Frame object) {
                    SwingUtilities.invokeLater(() -> {
                        logTextArea.append(new String(object.getPayload()));
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    SwingUtilities.invokeLater(() -> {
                        logTextArea.append("ERROR: " + throwable.getLocalizedMessage() + "\n");
                    });
                }

                @Override
                public void onComplete() {
                    SwingUtilities.invokeLater(() -> {
                        logTextArea.append("Complete !\n"); 
                    });
                }

                @Override
                public void close() throws IOException {
                    SwingUtilities.invokeLater(() -> {
                        logTextArea.append("Closed.\n"); 
                    });
                }
            };
            dockerClient.logContainerCmd(container.getId())
                .withFollowStream(true)
                .withStdOut(true)
                .withStdErr(true)
                .exec(resultCallback);

            while (true) {
                InspectContainerResponse containerRes = dockerClient.inspectContainerCmd(container.getId()).exec();
                HealthState health = containerRes.getState().getHealth();
                
                if (health != null && "healthy".equalsIgnoreCase(health.getStatus())) {
                    resultCallback.close(); // Close log streaming
                    return "Container is healthy!";
                }

                Thread.sleep(1000);
            }
        } catch (NotFoundException | IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private Container getContainer() {
       List<String> names = new ArrayList<>();
       names.add(CONTAINER_NAME);
       List<Container> containers = dockerClient.listContainersCmd()
            .withShowSize(true)
            .withShowAll(true)
               .withNameFilter(names)
               .exec();
       if(!containers.isEmpty()) {
           return containers.get(0);
       }
       return null;
    }
    
    private State getState(Container container) {
        return State.valueOf(container.getState());
    }
   
    private enum State {
        created, // A container that has been created (e.g. with docker create) but not started
        restarting, // A container that is in the process of being restarted
        running, // A currently running container
        paused, // A container whose processes have been paused
        exited, // A container that ran and completed ("stopped" in other contexts, although a created container is technically also "stopped")
        dead, // A container that the daemon tried and failed to stop (usually due to a busy device or resource used by the container) - Added on v1.22
    }
}
