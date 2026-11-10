# Castor Source Code

This repository contains the source-code for our CANS'26 paper "Static Detection of GTFO Binaries using Dataflow Patterns".

## Project Structure

- `queries/` contains the Joern queries to find GTFO functionalities in C programs
- `scripts/cpgqls_client_custom` contains our forked joern python client
  - this is slightly modified version of the code in https://github.com/joernio/cpgqls-client-python/tree/master
  - Please also refer to the License section below
- `data/` meta-data and labels
  - the files in `data/programs.yaml` list the binaries
    - including their `package` name
    - and if they are in scope the `path` to file with the main function (`progPath` in the queries)
- `setup/` contains scripts for setting the pipeline up
  - includes building the CPGs
- `results/` contains our raw results and summary logs
  
## Setup & Usage

We assume a client-server setup with multiple joern's running in server mode.

### Server:

- Dependencies:
  - joern must be installed and ready to use, ref. to https://github.com/joernio/joern

- Corpus: to download the source code and build CPGs, run these commands
  - `get_source_code` first downloads the source code and then runs configuration - this requires the installation of various packages and is not included in the script. Set `CONFIGURE=false` in the script to only download the source code

```shell
cd setup
./get_source_code
./build_cpg
```

- to start the joern servers, run `./start_servers
  - 8 servers are started on ports 8080-8087, if a server is killed, it is restarted as long as the script is running

### Client

- Dependencies:
  - conda

Setup:

```shell
cd setup
conda env create -f joern-env.yaml
conda activate joern
```

To run e.g. the query 'shell' on all programs and save the resuls under `results/shell`:

```shell
cd ../scripts
python run_query_on_server --query shell --dir shell
```

Further command line parameters are availabe (e.g. run only on a specific package or only test programs)

## License

The client in `scripts/cpgqls_client_custom` is forked from https://github.com/joernio/cpgqls-client-python which is licensed under Apache-2.0.
Therefore, the contents under that folder are licensed under Apache-2.0. as well, see its LICENSE file.

The rest of this repository is licensed under GPLv3, see the LICENSE file.

## Cite Us!

To appear at the 25th International Conference on Cryptology and Network Security, ref. https://uow-ic2.github.io/cans2026/accepted.html
