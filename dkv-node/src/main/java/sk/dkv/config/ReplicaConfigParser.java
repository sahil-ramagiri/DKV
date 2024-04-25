package sk.dkv.config;


import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReplicaConfigParser {


    private ReplicaConfigParser() {
    }

    public static ReplicaConfig parseArgs(String[] args) {
        List<Integer> ports = new ArrayList<>();
        Integer index = null;
        Path dataDir = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--ports" -> {
                    if (i + 1 >= args.length) {
                        log.error("Missing value for --ports option.");
                        printHelpAndExit();
                    }
                    try {
                        String[] portsArray = args[++i].split(",");
                        for (String portString : portsArray) {
                            int port = Integer.parseInt(portString.trim());
                            ports.add(port);
                        }
                    } catch (NumberFormatException e) {
                        log.error("Invalid port number format. Port numbers should be integers separated by commas.");
                        printHelpAndExit();
                    }
                }
                case "--index" -> {
                    if (i + 1 >= args.length) {
                        log.error("Missing value for --index option.");
                        printHelpAndExit();
                    }
                    try {
                        index = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        log.error("Invalid index format. Index should be an integer.");
                        printHelpAndExit();
                    }
                }
                case "--data" -> {
                    if (i + 1 >= args.length) {
                        log.error("Missing value for --data option.");
                        printHelpAndExit();
                    }
                    dataDir = Paths.get(args[++i]);
                    if (!dataDir.toFile().isDirectory()) {
                        log.error(STR."Invalid data directory: \{dataDir}");
                        printHelpAndExit();
                    }
                }
                case "--debug" -> {
//                    org.apache.logging.log4j.core.config.Configurator.setRootLevel(Level.DEBUG);
                    log.debug("Enable debug output");
                }
                default -> {
                    log.error(STR."Invalid option: \{args[i]}");
                    printHelpAndExit();
                }
            }
        }

        if (ports.isEmpty() || index == null || dataDir == null) {
            log.error("Missing required options.");
            printHelpAndExit();
        }

        if (index < 0 || index >= ports.size()) {
            log.error(STR."Index out of bounds. Index should be between 0 and \{ports.size() - 1}");
            printHelpAndExit();
        }

        ReplicaConfig config = new ReplicaConfig(ports, index, dataDir);
        log.info(STR."Parsed Configuration: \{config}");

        return config;
    }

    private static void printHelpAndExit() {
        System.out.println("Usage: java PortNumberParser --ports <port_numbers> --index <index> --data <data_dir>");
        System.out.println("  --ports <port_numbers>: Comma-separated list of port numbers");
        System.out.println("  --index <index>: Index of the port to use");
        System.out.println("  --data <data_dir>: Path to the data directory");
        System.exit(1);
    }
}

