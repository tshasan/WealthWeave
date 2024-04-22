#!/bin/bash

# Function to find Android SDK path for macOS
find_sdk_mac() {
    # Default path for macOS
    echo "sdk.dir=/Users/$(whoami)/Library/Android/sdk"
}

# Function to find Android SDK path for Linux
find_sdk_linux() {
    # Default path for Linux
    echo "sdk.dir=/home/$(whoami)/Android/Sdk"
}

# Detect the operating system
OS="$(uname -s)"
case "$OS" in
    Darwin) # macOS
        find_sdk_mac > local.properties
        ;;
    Linux) # Linux
        find_sdk_linux > local.properties
        ;;
    *)
        echo "Unsupported operating system."
        exit 1
        ;;
esac

echo "local.properties created successfully."
