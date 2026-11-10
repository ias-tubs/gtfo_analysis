#!/bin/bash

LOG=error.log
: > $LOG

RUN_DIR="$(pwd)"

cd ../source_code
CODE_DIR="$(pwd)"

cd ".."
mkdir -p source_cpgs
cd source_cpgs
CPG_DIR="$(pwd)"

build_cpg()
{   
    PROJECT=$1
    EXCLUDE=$2
    DEFINE=$3
    EXCLUDE_OPTIONS=""
    if (( ${#EXCLUDE} > 0 )); then
        EXCLUDE_OPTIONS=" --exclude ${EXCLUDE// / --exclude }"
    fi

    DEFINE_OPTIONS=""
    if (( ${#DEFINE} > 0 )); then
        DEFINE_OPTIONS=" --define ${DEFINE// / --define }"
    fi

    # COMP_JSON_OPTIONS=""
    # if (( ${#COMP_JSON} > 0 )); then
    #     COMP_JSON_OPTIONS=" --compilation-database ${COMP_JSON//+/}compile_commands.json"
    # fi

    cd $CPG_DIR

    if [ ! -e "${PROJECT%%/*}.bin" ]; then
        c2cpg.sh -J-Xmx100G -J-Xss128m $CODE_DIR/$PROJECT --output ${PROJECT%%/*}.bin --with-include-auto-discovery$EXCLUDE_OPTIONS$DEFINE_OPTIONS
    else
        echo "CPG for $PROJECT already exists."
        return
    fi
    if [ $? = 1 ]; then
        echo "Error generating CPG for $PROJECT."
        echo "Error generating CPG for $PROJECT." > $RUN_DIR/$LOG
    else
        echo "CPG for $PROJECT was generated."
    fi
}

build_cpg acl "test examples"
build_cpg alpine
build_cpg apache2 "test"
build_cpg arj
build_cpg at

build_cpg basez "tests"
build_cpg bash "tests examples"
build_cpg bc "Test"
build_cpg bconsole/bacula/src
build_cpg bind9 "tests bin/tests"
build_cpg binutils "gold binutils/testsuite"
build_cpg busybox "testsuite"
build_cpg bzip2 "" "FILE_OFFSET_BITS=64"

build_cpg cdrkit
build_cpg clamav "unit_tests examples"
build_cpg coreutils "tests gnulib-tests"
build_cpg cpio "tests"
build_cpg cpulimit
build_cpg cpython "Doc"
build_cpg crash
build_cpg cron
build_cpg csh
build_cpg cups "test"
build_cpg curl "tests docs"

build_cpg daemontools/daemontools-0.76/src
build_cpg dash
build_cpg debianutils
build_cpg dialog "package po samples"
build_cpg diffutils "tests gnulib-tests"
build_cpg distcc "test"
build_cpg dmidecode
build_cpg dos2unix "test"
build_cpg dpkg "tests"

build_cpg e2fsprogs "tests"
build_cpg ed "testsuite"
build_cpg emacs "test"
build_cpg enscript
build_cpg expect "tests testsuite"

build_cpg file/src
build_cpg findutils "tests gnulib-tests"
build_cpg finger
build_cpg fping

build_cpg gawk "test"
#build_cpg gcc "gcc/testsuite" did not build in 10 min
build_cpg gdb "gdb/testsuite gdb/unittests"
#build_cpg gettext "gettext-tools/gnulib-tests gettext-tools/system-tests gettext-tools/tests" did not build in 10 min
build_cpg gimp
build_cpg git
build_cpg glib "tests glib/tests"
build_cpg glibc
build_cpg gnucobol "tests"
build_cpg grep "tests gnulib-tests"
build_cpg gzip "tests"

build_cpg hostname
build_cpg hping3 "" "BYTE_ORDER_LITTLE_ENDIAN=1"

build_cpg iftop
build_cpg inetutils "tests"
build_cpg iproute2 "testsuite"
build_cpg iputils "test"
build_cpg ispell

build_cpg joe/joe
build_cpg jq/src

build_cpg kbd "tests"
build_cpg krb5/src

build_cpg less
build_cpg libcap2-bin/progs
build_cpg libvirt "tests"
build_cpg links
build_cpg linux/tools
build_cpg ltrace "testsuite"
build_cpg lua
build_cpg lvm2 "test"

build_cpg mailutils "testsuite"
build_cpg make "tests"
build_cpg man-db
build_cpg mawk "test"
build_cpg minicom
build_cpg mksh
build_cpg mosquitto "test"
build_cpg mtr "test"
build_cpg multitime

build_cpg nano
#build_cpg nasm "test" did not build in 10 min
build_cpg ncftp
build_cpg ncurses "test"
build_cpg net-tools
build_cpg netcat-bsd
build_cpg netcat-traditional "" "GAPING_SECURITY_HOLE"
build_cpg netkit-rsh
build_cpg nftables "tests"
build_cpg ntp "tests"

build_cpg openssh
build_cpg openssl "test"
build_cpg openvpn "tests"
build_cpg openwince-jtag
build_cpg opkg "tests"
build_cpg original-awk "testdir"

build_cpg pax
build_cpg pexec/src
build_cpg php "tests"
build_cpg pkg "tests"
build_cpg polkit "test"
build_cpg posh "tests"
build_cpg postgres
build_cpg procps "testsuite"
build_cpg putty "test"

build_cpg rc
build_cpg redis "tests"
build_cpg rlwrap "test"
build_cpg rpm "tests"
build_cpg rsync "testsuite"
build_cpg ruby "test"

build_cpg samba "selftest testdata tests testsuite testprogs"
build_cpg sash "" "HAVE_GZIP=1 HAVE_LINUX_ATTR=1 HAVE_LINUX_CHROOT=1 HAVE_LINUX_LOSETUP=1 HAVE_LINUX_PIVOT=1 HAVE_LINUX_MOUNT=1 HAVE_BSD_MOUNT=0 MOUNT_TYPE='ext3'"
build_cpg scanmem
build_cpg screen
build_cpg scrot
build_cpg sed "gnulib-tests testsuite"
build_cpg shadow-utils "tests"
build_cpg sharutils "tests"
build_cpg slang2
build_cpg socat
build_cpg squashfs-tools/squashfs-tools
build_cpg sshpass
build_cpg strace "tests tests-m32 tests-mx32"
build_cpg sudo
build_cpg sysstat "tests"
build_cpg systemd/src

build_cpg tar "tests"
build_cpg tcpdump "tests"
build_cpg tdb-tools "test"
build_cpg texlive-bin
build_cpg tftp-hpa
build_cpg time "tests"
build_cpg tk "win macosx tests"
build_cpg tmate
build_cpg tmux
build_cpg tor

build_cpg unzip
build_cpg util-linux "tests"

build_cpg valgrind "tests cachegrind/tests none/tests callgrind/tests dhat/tests helgrind/tests"
build_cpg varnish
build_cpg vim/src

build_cpg w3m "tests"
build_cpg wget "tests testenv"
build_cpg whiptail
build_cpg whois
build_cpg wireshark "test"

build_cpg x11-apps
build_cpg x11-xserver-utils
build_cpg xdotool
build_cpg xpad
build_cpg xz "tests"

build_cpg yash "tests"
build_cpg yelp "tests"

build_cpg zathura "tests"
build_cpg zip "tests"
build_cpg zsh "Test"
