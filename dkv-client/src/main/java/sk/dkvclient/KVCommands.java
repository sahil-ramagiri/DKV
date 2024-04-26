package sk.dkvclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@Slf4j
public class KVCommands {

    private final LeaderManager leaderManager;

    public KVCommands(LeaderManager leaderManager) {
        this.leaderManager = leaderManager;
    }

    @ShellMethod(key = "connect")
    public String connect(@ShellOption(defaultValue = "3000,3001,3002") List<Integer> ports) {
        List<String> hosts = ports.stream().map("localhost:%s"::formatted).toList();
        leaderManager.setHosts(hosts);
        return STR."Connected to \{leaderManager.getHosts()}";
    }

    @ShellMethod(key = "get")
    public String get(@ShellOption String key) {
        String value = leaderManager.getRequest(key);
        return STR."GET key: \{key} , value: \{value}";
    }

    @ShellMethod(key = "set")
    public String put(@ShellOption String key, @ShellOption String value) {
        String oldValue = leaderManager.setRequest(key, value);
        return STR."SET key: \{key} , value: \{value}, oldValue: \{oldValue}";
    }

    @ShellMethod(key = "clear")
    public String clear(@ShellOption String key) {
        String oldValue = leaderManager.clearRequest(key);
        return STR."CLEAR key: \{key} , value: \{oldValue}";
    }

}