import * as kubernetes from "@pulumi/kubernetes";
import * as pulumi from "@pulumi/pulumi";

// Create a Kubernetes Namespace
const ns = new kubernetes.core.v1.Namespace("rabbitmq-ns", {
    metadata: {
        name: "rabbitmq"
    }
});

// Deploy RabbitMQ with the management plugin enabled
const rabbitmq = new kubernetes.helm.v3.Chart("rabbitmq", {
    chart: "rabbitmq",
    version: "8.11.3",
    namespace: ns.metadata.name,
    fetchOpts: {
        repo: "https://charts.bitnami.com/bitnami",
    },
    values: {
        auth: {
            username: "user",
            password: "password", // Replace with a secure password
        },
        service: {
            type: "ClusterIP", // Exposes RabbitMQ within the Kubernetes cluster
        },
        extraPlugins: "rabbitmq_management", // Enables the management UI
    },
}, { provider: k8sProvider });

// Export the URL to access the RabbitMQ management UI
export const rabbitmqManagementUrl = pulumi.interpolate`http://${rabbitmq.getStatus().apply(status => status.loadBalancer.ingress[0].ip)}:15672`;