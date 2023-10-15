package jamuz.soulseek;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author raph
 */
public class SlskdDocker {
    
//    https://github.com/slskd/slskd/tree/master
//    https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md
//    https://www.baeldung.com/docker-java-api
    
    //FIXME !! Turn this into options
    private final boolean SLSKD_SWAGGER ;
    private final String SLSKD_SLSK_USERNAME;
    private final String SLSKD_SLSK_PASSWORD;
    private final String serverPath;
    private DockerClient dockerClient;
    
    private static final String CONTAINER_NAME = "jamuz-slskd";

    public SlskdDocker(boolean SLSKD_SWAGGER, String SLSKD_SLSK_USERNAME, String SLSKD_SLSK_PASSWORD, String serverPath) {
        this.SLSKD_SWAGGER = SLSKD_SWAGGER;
        this.SLSKD_SLSK_USERNAME = SLSKD_SLSK_USERNAME;
        this.SLSKD_SLSK_PASSWORD = SLSKD_SLSK_PASSWORD;
        this.serverPath = serverPath;
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }
    
    public boolean start() {
        //FIXME ! Start (and check errors) docker image slskd
        // And also, set sharing
//			shares:
//			directories:
//			  - /home/xxx/Musique/Archive/
        Container container = getContainer();
        if(container == null) {
            createAndStartContainer();
            return true;
        } else {
            State state = getState(container);
            switch (state) {
                case dead:
                case exited:
                    dockerClient.killContainerCmd(container.getId()).exec();
                    createAndStartContainer();
                    return true;
                case paused:
                    dockerClient.stopContainerCmd(container.getId()).exec();
                    dockerClient.startContainerCmd(container.getId()).exec();
                    return true;
                case created:
                case restarting:
                    //FIXME !! Wait
                    break;
                case running:
                    return true;
                default:
                    throw new AssertionError();
            }
            return false;
        }
    }
    
    private void createAndStartContainer() {
        CreateContainerResponse container
          = dockerClient.createContainerCmd("slskd/slskd:latest")
            .withName(CONTAINER_NAME)
            .withEnv("SLSKD_REMOTE_CONFIGURATION=true",
                    "SLSKD_SLSK_USERNAME="+SLSKD_SLSK_USERNAME,
                    "SLSKD_SLSK_PASSWORD="+SLSKD_SLSK_PASSWORD,
                    "SLSKD_SWAGGER="+SLSKD_SWAGGER)
            .withExposedPorts(ExposedPort.tcp(5030), ExposedPort.tcp(5031), ExposedPort.tcp(50300))
            .withHostConfig(HostConfig.newHostConfig()
                .withPortBindings(
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5030), ExposedPort.tcp(5030)),
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 5031), ExposedPort.tcp(5031)),
                    new PortBinding(Ports.Binding.bindIpAndPort("0.0.0.0", 50300), ExposedPort.tcp(50300))
                )
                .withBinds(Bind.parse(serverPath+":/app"))
            )
            .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
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
