#!/usr/bin/env bash
# JDK 25 LTS をデフォルトにする
# Codespaces の標準ベースイメージは update-alternatives で複数の Java を管理しているため、
# 起動後にこのスクリプトを実行して Java 25 を選択する。
set -euo pipefail

JDK25_HOME=/usr/lib/jvm/temurin-25-jdk-amd64

sudo update-alternatives --set java "${JDK25_HOME}/bin/java"
sudo update-alternatives --set javac "${JDK25_HOME}/bin/javac"
export JAVA_HOME="${JDK25_HOME}"

java -version
javac -version
