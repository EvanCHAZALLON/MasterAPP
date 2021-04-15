package net.evan.masterapp.utils;

import com.github.dockerjava.api.model.*;

import java.util.*;

public class DockerService {
    private final String name;

    private final String imageId;
    private final String network;

    private String hostname;

    private final Map<String, String> mount;

    private int publishedPort;
    private int targetPort;

    public DockerService(String name, String imageId, String network) {
        this.name = name;
        this.imageId = imageId;
        this.network = network;
        this.mount = new HashMap<>();
    }


    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public String getNetwork() {
        return network;
    }

    public void addMount(String local, String container) {
        this.mount.put(local, container);
    }

    public void removeMount(String local, String container) {
        this.mount.remove(local, container);
    }

    public Map<String, String> getMount() {
        return mount;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public ServiceSpec toSwarmService() {

        ContainerSpec containerSpec = new ContainerSpec()
                .withImage(this.imageId)
                .withHostname(this.hostname);
        List<NetworkAttachmentConfig> networks = new ArrayList<>();
        networks.add(
                new NetworkAttachmentConfig()
                        .withTarget(this.network)
        );

        EndpointSpec endpointSpec = new EndpointSpec()
                .withMode(EndpointResolutionMode.VIP)
                .withPorts(Arrays.asList(
                        new PortConfig()
                                .withProtocol(PortConfigProtocol.TCP)
                                .withPublishedPort(this.publishedPort)
                                .withTargetPort(this.targetPort)
                ));

        TaskSpec taskSpec = new TaskSpec()
                .withContainerSpec(containerSpec)
                .withNetworks(networks);
        ServiceSpec serviceSpec = new ServiceSpec();
        serviceSpec.withName(this.name)
                .withTaskTemplate(taskSpec)
                .withEndpointSpec(endpointSpec);

        return serviceSpec;
    }
}
