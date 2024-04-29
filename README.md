# DKV
Distributed Key Value Store with fault tolerance.

## Usage

Start Node

- `java -jar dkv-node.jar --index 0  --ports 3000,3001,3002 --dataDir node0`
- `java -jar dkv-node.jar --index 1  --ports 3000,3001,3002 --dataDir node1`
- `java -jar dkv-node.jar --index 2  --ports 3000,3001,3002 --dataDir node2` 

Start Client

`java -jar dkv-client.jar` 

Connect Client to cluster

`> connect`

Get Operation

`get --key <key>`

Set Operation

`set --key <key> --value <value>`

Clear Operation

`clear --key <key>`