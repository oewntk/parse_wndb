#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

set -e

# env variables
# WNHOME20
# WNHOME21
# WNHOME30
# WNHOME31
# WNHOMEXX

INDIR="$1"
if [[ "${INDIR}" =~ ^[[:digit:]] ]]; then
	case ${INDIR} in
		20) INDIR="${WNHOME20}" ;;
		21) INDIR="${WNHOME21}" ;;
		30) INDIR="${WNHOME30}" ;;
		31) INDIR="${WNHOME31}" ;;
		00) INDIR="${WNHOMEXX}" ;;
	esac
else
	if [ -z "${INDIR}" ]; then
		INDIR=out
	fi
fi

echo "${INDIR}"
jar=parse_wndb-3.0.1-uber.jar
if [ ! -e "${jar}" ]; then
  if [ ! -e "target/${jar}" ]; then
    echo "Non existing uber jar" >&2
    exit 1
    fi
  ln -s "target/${jar}"
  fi
if [ ! -e "${jar}" ]; then
  echo "Non existing uber jar" >&2
  exit 2
  fi
java -ea -DSILENT -cp "${jar}" org.oewntk.parse.Parser "${INDIR}"
