package demo;

import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;


@Component
@ConfigurationProperties(prefix = "spring.neo4j")
class Neo4jProperties {

    private String host;
    private int port;
    private String password;
    private String username;

    private String uri;

    public String getHost() {
        return host;
    }

    public String getUri() {
        return uri;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void setup() {
        Assert.hasText(this.host, "host is required");
        Assert.isTrue(this.port > 0, "port is required");

        if (!StringUtils.hasText(this.uri)) {
            this.uri = String.format("http://%s:%s", this.host, this.port);
        }
        LogFactory.getLog(getClass()).info(String.format("host=%s, port=%s, uri=%s", this.host, this.port, this.uri));
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
