#!/usr/bin/env bash

FILE="sql/visits_v1.tsv"

if [ -f "$FILE" ]; then
    echo "$FILE exists."
else
  echo "# Start loading $FILE ...."
  curl https://datasets.clickhouse.com/visits/tsv/visits_v1.tsv.xz | unxz --threads=`nproc` > $FILE  
fi
