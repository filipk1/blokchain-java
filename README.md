# Simple blockchain implementation

This project was created from scratch for interview purposes. It was meant to be holding artifical blockchain and provide REST endpoints for dumping entire blockchain + finding given transaction if present.

Due to the fact that I find blockchain technology interesting I decided to add couple of features simulating typical node activity.

- Implemented transactions flow from Amazon SQS
- Restricted number of transactions per block to 10 (configurable)
- Simulated block mining complexity (configurable)
- Block can be mined only if there are transactions present
- Mining and validation are the same due to the fact that intruducing asymetric algo would prevent me from finishing in time


Blockchain is held in-memory, as in real node it's not using ie. Amazon RDS/DymnamoDB due to decentralization. Possible (and probably smart) to extend it with local DB.

REST endpoints:
- /blocks - dumps entire chain
- /mine - mines new block and adds it to the chain
- /validate - validate transaction by searching the chain and returns entire block if found

Features not present that I'd like to tackle:
- Separate mine from validate and distribute mined block for validation to other nodes
- Blockchain broadcast/validation (we can dump it but there is no mechanism to compare/load)
- Consensus protocol
