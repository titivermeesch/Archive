#!/bin/zsh

# Connect via ssh
colima ssh << EOF
# Switch to superuser
sudo su -
sudo ip addr add 192.168.49.2/24 dev col0
sudo ip link set col0 up
sudo ip route add default via 192.168.49.1
sudo ip link set col0 down
sudo ip link set col0 up
ip addr show col0
ip route show
EOF
