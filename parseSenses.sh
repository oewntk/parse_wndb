#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

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
jar=target/parse_wndb-2.2.2-uber.jar
java -ea -cp "${jar}" org.oewntk.parse.SenseParser "${INDIR}"
