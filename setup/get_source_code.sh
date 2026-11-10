#!/bin/bash

LOG=error.log
: > $LOG

RUN_DIR="$(pwd)"

cd ".."
mkdir -p "source_code"
cd "source_code"

SOURCE_DIR="$(pwd)"

CONFIGURE=true

#for autotools projects
configure()
{   
    if [ $CONFIGURE = false ]; then
        return
    fi
    PROJECT=$1
    cd $PROJECT
    ./configure $2
    if [ ! $? = 0 ]; then
        echo "Error configuring $PROJECT" >> $RUN_DIR/$LOG
    fi
    cd $SOURCE_DIR
}
run_autogen()
{   
    if [ $CONFIGURE = false ]; then
        return
    fi
    PROJECT=$1
    cd $PROJECT
    ./autogen.sh
    if [ ! $? = 0 ]; then
        echo "Error autoreconfing $PROJECT" >> $RUN_DIR/$LOG
    fi
    cd $SOURCE_DIR
}
run_autoreconf()
{   
    if [ $CONFIGURE = false ]; then
        return
    fi
    PROJECT=$1
    cd $PROJECT
    autoreconf -fi
    if [ ! $? = 0 ]; then
        echo "Error autoreconfing $PROJECT" >> $RUN_DIR/$LOG
    fi
    cd $SOURCE_DIR
}

run_cmake()
{   
    if [ $CONFIGURE = false ]; then
        return
    fi
    PROJECT=$1
    cd $PROJECT
    cmake -S . -B build $2
    if [ ! $? = 0 ]; then
        echo "Error cmaking $PROJECT" >> $RUN_DIR/$LOG
    fi
    cd $SOURCE_DIR
}

run_meson()
{   
    if [ $CONFIGURE = false ]; then
        return
    fi
    PROJECT=$1
    cd $PROJECT
    meson setup build
    if [ ! $? = 0 ]; then
        echo "Error mesoning $PROJECT" >> $RUN_DIR/$LOG
    fi
    cd $SOURCE_DIR
}

#generates compile_commands.json used to generate CPG
# run_bear()
# {   
#     if [ $BEAR = false ]; then
#         return
#     fi
#     PROJECT=$1
#     cd $PROJECT
#     bear -- make
#     if [ ! $? = 0 ]; then
#         echo "Error running bear in $PROJECT" >> $RUN_DIR/$LOG
#     fi
#     cd $SOURCE_DIR
# }

#acl for acl
if [ ! -d "acl" ]; then
    wget "https://download.savannah.nongnu.org/releases/acl/acl-2.3.2.tar.gz"
    tar -xvzf "acl-2.3.2.tar.gz"
    rm "acl-2.3.2.tar.gz"
    mv "acl-2.3.2" "acl"
    configure acl
fi

#alpine for alpine and alpine-pico
ALPINE_VER="2.26"
if [ ! -d "alpine" ]; then
    wget https://alpineapp.email/alpine/release/src/alpine-${ALPINE_VER}.tar.xz
    tar -xvf "alpine-${ALPINE_VER}.tar.xz"
    rm "alpine-${ALPINE_VER}.tar.xz"
    mv "alpine-${ALPINE_VER}" "alpine"
    configure alpine
fi

#apache2 for apache2-utils, apache2, apache2-suexec-custom
if [ ! -d "apache2" ]; then
    wget "https://salsa.debian.org/apache-team/apache2/-/archive/debian/2.4.25-3+deb9u21/apache2-debian-2.4.25-3+deb9u21.tar.gz"
    tar -xvzf "apache2-debian-2.4.25-3+deb9u21.tar.gz"
    rm "apache2-debian-2.4.25-3+deb9u21.tar.gz"
    mv "apache2-debian-2.4.25-3+deb9u21" "apache2"
    configure apache2
fi

#arj for arj
if [ ! -d "arj" ]; then
    wget "https://arj.sourceforge.net/files/arj-3.10.22.tar.gz"
    tar -xvzf "arj-3.10.22.tar.gz"
    rm "arj-3.10.22.tar.gz"
    mv "arj-3.10.22" "arj"
    if [ $CONFIGURE = true ]; then
        cd arj/gnu
        autoheader
        autoconf
        cp /usr/share/automake-1.16/config.sub config.sub
        ./configure
        cd $SOURCE_DIR 
    fi
fi

#at for at
if [ ! -d "at" ]; then
    wget "https://salsa.debian.org/debian/at/-/archive/debian/3.2.5-2/at-debian-3.2.5-2.tar.gz"
    tar -xvzf "at-debian-3.2.5-2.tar.gz"
    rm "at-debian-3.2.5-2.tar.gz"
    mv "at-debian-3.2.5-2" "at"
    configure at "--with-jobdir=/var/spool --with-atspool=/var/spool"
fi

#basez for basez
if [ ! -d "basez" ]; then
    wget "https://salsa.debian.org/debian/basez/-/archive/debian/1.6.2-3/basez-debian-1.6.2-3.tar.gz"
    tar -xvzf "basez-debian-1.6.2-3.tar.gz"
    rm "basez-debian-1.6.2-3.tar.gz"
    mv "basez-debian-1.6.2-3" "basez"
    configure basez
fi

#bash for bash
if [ ! -d "bash" ]; then
    wget "https://github.com/bminor/bash/archive/refs/tags/bash-5.3.tar.gz"
    tar -xvzf "bash-5.3.tar.gz"
    rm "bash-5.3.tar.gz"
    mv "bash-bash-5.3" "bash"
    configure bash
fi

#bc for bc and dc
if [ ! -d "bc" ]; then
    wget "https://salsa.debian.org/debian/bc/-/archive/debian/1.07.1-4/bc-debian-1.07.1-4.tar.gz"
    tar -xvzf "bc-debian-1.07.1-4.tar.gz"
    rm "bc-debian-1.07.1-4.tar.gz"
    mv "bc-debian-1.07.1-4" "bc"
    configure bc
fi

#bconsole for _bconsole
if [ ! -d "bconsole" ]; then
    wget "http://gitlab.bacula.org/bacula-community-edition/bacula-community/-/archive/Release-15.0.2/bacula-community-Release-15.0.2.tar.gz"
    tar -xvzf "bacula-community-Release-15.0.2.tar.gz"
    rm "bacula-community-Release-15.0.2.tar.gz"
    mv "bacula-community-Release-15.0.2" "bconsole"
    configure bconsole/bacula "--enable-client-only"
fi

#bind9 for bind9, bind9-utils, bind9-host, bind9-dnsutils
if [ ! -d "bind9" ]; then
    wget "https://gitlab.isc.org/isc-projects/bind9/-/archive/v9.18.39/bind9-v9.18.39.tar.gz"
    tar -xvzf "bind9-v9.18.39.tar.gz"
    rm "bind9-v9.18.39.tar.gz"
    mv "bind9-v9.18.39" "bind9"
    run_autoreconf bind9
    configure bind9
fi

#binutils for binutils
if [ ! -d "binutils" ]; then
    wget "https://ftp.gnu.org/gnu/binutils/binutils-with-gold-2.44.tar.xz"
    tar -xvf "binutils-with-gold-2.44.tar.xz"
    rm "binutils-with-gold-2.44.tar.xz"
    mv "binutils-with-gold-2.44" "binutils"
    configure binutils
fi

#busybox for busybox
if [ ! -d "busybox" ]; then
    wget "https://github.com/mirror/busybox/archive/refs/tags/1_36_1.tar.gz"
    tar -xvzf "1_36_1.tar.gz"
    rm "1_36_1.tar.gz"
    mv "busybox-1_36_1" "busybox"
    #generate default config
    if [ $CONFIGURE = true ]; then
        cd busybox
        make defconfig
        cd $SOURCE_DIR  
    fi
fi

#bzip2 for bzip2
if [ ! -d "bzip2" ]; then
    wget "https://github.com/libarchive/bzip2/archive/refs/tags/bzip2-1.0.8.tar.gz"
    tar -xvzf "bzip2-1.0.8.tar.gz"
    rm "bzip2-1.0.8.tar.gz"
    mv "bzip2-bzip2-1.0.8" "bzip2"
    #custom build - no config - variable in CPG creation
fi

#cdrkit for genisoimage, wodim, icedax
if [ ! -d "cdrkit" ]; then
    wget "http://deb.debian.org/debian/pool/main/c/cdrkit/cdrkit_1.1.11.orig.tar.gz"
    tar -xvzf "cdrkit_1.1.11.orig.tar.gz"
    rm "cdrkit_1.1.11.orig.tar.gz"
    mv "cdrkit-1.1.11" "cdrkit"
    run_cmake cdrkit
fi

#clamav for clamav
if [ ! -d "clamav" ]; then
    wget "https://github.com/Cisco-Talos/clamav/releases/download/clamav-1.4.3/clamav-1.4.3.tar.gz"
    tar -xvzf "clamav-1.4.3.tar.gz"
    rm "clamav-1.4.3.tar.gz"
    mv "clamav-1.4.3" "clamav"
    run_cmake clamav
fi

#coreutils for coreutils
if [ ! -d "coreutils" ]; then
    wget "https://ftp.gnu.org/gnu/coreutils/coreutils-9.9.tar.gz"
    tar -xvzf "coreutils-9.9.tar.gz"
    rm "coreutils-9.9.tar.gz"
    mv "coreutils-9.9" "coreutils"
    configure coreutils
fi

#cpio for cpio
if [ ! -d "cpio" ]; then
    wget "https://salsa.debian.org/lamby/pkg-cpio/-/archive/debian/2.13+dfsg-2/pkg-cpio-debian-2.13+dfsg-2.tar.gz"
    tar -xvzf "pkg-cpio-debian-2.13+dfsg-2.tar.gz"
    rm "pkg-cpio-debian-2.13+dfsg-2.tar.gz"
    mv "pkg-cpio-debian-2.13+dfsg-2" "cpio"
    configure cpio
fi

#cpulimit for cpulimit
if [ ! -d "cpulimit" ]; then
    wget "https://sourceforge.net/projects/limitcpu/files/limitcpu/cpulimit-3.1.tar.gz"
    tar -xvzf "cpulimit-3.1.tar.gz"
    rm "cpulimit-3.1.tar.gz"
    mv "cpulimit-3.1" "cpulimit"
    #custom build - no config
fi

#cpython for python3
if [ ! -d "cpython" ]; then
    wget "https://github.com/python/cpython/archive/refs/tags/v3.14.0rc3.tar.gz"
    tar -xvzf "v3.14.0rc3.tar.gz"
    rm "v3.14.0rc3.tar.gz"
    mv "cpython-3.14.0rc3" "cpython"
    configure cpython
fi

#crash for crash
if [ ! -d "crash" ]; then
    wget "https://github.com/crash-utility/crash/archive/refs/tags/9.0.0.tar.gz"
    tar -xvzf "9.0.0.tar.gz"
    rm "9.0.0.tar.gz"
    mv "crash-9.0.0" "crash"
    #custom build - no config
fi

#cron for cron
if [ ! -d "cron" ]; then
    wget "https://salsa.debian.org/debian/cron/-/archive/debian/3.0pl1-198/cron-debian-3.0pl1-198.tar.gz"
    tar -xvzf "cron-debian-3.0pl1-198.tar.gz"
    rm "cron-debian-3.0pl1-198.tar.gz"
    mv "cron-debian-3.0pl1-198" "cron"
    #custom build
fi

#csh for csh
if [ ! -d "csh" ]; then
    wget "https://salsa.debian.org/mckinstry/bsd-csh/-/archive/debian/20240808-3/bsd-csh-debian-20240808-3.tar.gz"
    tar -xvzf "bsd-csh-debian-20240808-3.tar.gz"
    rm "bsd-csh-debian-20240808-3.tar.gz"
    mv "bsd-csh-debian-20240808-3" "csh"
    #custom build
fi

#cups for cups, cups-client, cups-bsd, cups-ipp-utils, cups-daemon
if [ ! -d "cups" ]; then
    wget "https://github.com/OpenPrinting/cups/releases/download/v2.4.12/cups-2.4.12-source.tar.gz"
    tar -xvzf "cups-2.4.12-source.tar.gz"
    rm "cups-2.4.12-source.tar.gz"
    mv "cups-2.4.12" "cups"
    configure cups
fi

#curl for curl
if [ ! -d "curl" ]; then
    wget "https://github.com/curl/curl/releases/download/curl-8_15_0/curl-8.15.0.tar.xz"
    tar -xvf "curl-8.15.0.tar.xz"
    rm "curl-8.15.0.tar.xz"
    mv "curl-8.15.0" "curl"
    configure curl --with-openssl
fi

#daemontools for daemontools
if [ ! -d "daemontools" ]; then
    wget "https://salsa.debian.org/debian/daemontools/-/archive/debian/1%250.76-15/daemontools-debian-1%250.76-15.tar.gz"
    tar -xvzf "daemontools-debian-1%0.76-15.tar.gz"
    rm "daemontools-debian-1%0.76-15.tar.gz"
    mv "daemontools-debian-1%0.76-15" "daemontools"
    #custom build
fi

#dash for dash
if [ ! -d "dash" ]; then
    wget "https://salsa.debian.org/debian/dash/-/archive/debian/0.5.12-12/dash-debian-0.5.12-12.tar.gz"
    tar -xvzf "dash-debian-0.5.12-12.tar.gz"
    rm "dash-debian-0.5.12-12.tar.gz"
    mv "dash-debian-0.5.12-12" "dash"
    configure dash
fi

#debianutils for debianutils
if [ ! -d "debianutils" ]; then
    wget "https://salsa.debian.org/debian/debianutils/-/archive/debian/5.23.2/debianutils-debian-5.23.2.tar.gz"
    tar -xvzf "debianutils-debian-5.23.2.tar.gz"
    rm "debianutils-debian-5.23.2.tar.gz"
    mv "debianutils-debian-5.23.2" "debianutils"
    run_autoreconf debianutils
    configure debianutils
fi

#dialog for dialog
if [ ! -d "dialog" ]; then
    wget "https://invisible-island.net/archives/dialog/dialog-1.3-20250817.tgz"
    tar -xvzf "dialog-1.3-20250817.tgz"
    rm "dialog-1.3-20250817.tgz"
    mv "dialog-1.3-20250817" "dialog"
    configure dialog
fi

#diffutils for diffutils
if [ ! -d "diffutils" ]; then
    wget "https://ftp.gnu.org/gnu/diffutils/diffutils-3.12.tar.gz"
    tar -xvzf "diffutils-3.12.tar.gz"
    rm "diffutils-3.12.tar.gz"
    mv "diffutils-3.12" "diffutils"
    configure diffutils
fi

#distcc for distcc
if [ ! -d "distcc" ]; then
    wget "https://github.com/distcc/distcc/releases/download/v3.4/distcc-3.4.tar.gz"
    tar -xvzf "distcc-3.4.tar.gz"
    rm "distcc-3.4.tar.gz"
    mv "distcc-3.4" "distcc"
    configure distcc
fi

#dmidecode for dmidecode
if [ ! -d "dmidecode" ]; then
    wget "https://github.com/mirror/dmidecode/archive/refs/tags/dmidecode-3-6.tar.gz"
    tar -xvzf "dmidecode-3-6.tar.gz"
    rm "dmidecode-3-6.tar.gz"
    mv "dmidecode-dmidecode-3-6" "dmidecode"
    #custom build
fi


#dos2unix for dos2unix
if [ ! -d "dos2unix" ]; then
    wget "https://salsa.debian.org/debian/dos2unix/-/archive/debian/7.5.2-1/dos2unix-debian-7.5.2-1.tar.gz"
    tar -xvzf "dos2unix-debian-7.5.2-1.tar.gz"
    rm "dos2unix-debian-7.5.2-1.tar.gz"
    mv "dos2unix-debian-7.5.2-1" "dos2unix"
    #custom build
fi

#dpkg for dpkg
if [ ! -d "dpkg" ]; then
    wget "https://ftp.debian.org/debian/pool/main/d/dpkg/dpkg_1.22.21.tar.xz"
    tar -xvf "dpkg_1.22.21.tar.xz"
    rm "dpkg_1.22.21.tar.xz"
    mv "dpkg-1.22.21" "dpkg"
    configure dpkg
fi

#e2fsprogs for e2fsprogs
if [ ! -d "e2fsprogs" ]; then
    wget "https://github.com/tytso/e2fsprogs/archive/refs/tags/v1.47.3.tar.gz"
    tar -xvzf "v1.47.3.tar.gz"
    rm "v1.47.3.tar.gz"
    mv "e2fsprogs-1.47.3" "e2fsprogs"
    configure e2fsprogs
fi

#ed for ed
if [ ! -d "ed" ]; then
    wget "https://salsa.debian.org/debian/ed/-/archive/debian/1.22.2-1/ed-debian-1.22.2-1.tar.gz"
    tar -xvzf "ed-debian-1.22.2-1.tar.gz"
    rm "ed-debian-1.22.2-1.tar.gz"
    mv "ed-debian-1.22.2-1" "ed"
    configure ed
fi

#emacs for emacs-gtk
if [ ! -d "emacs" ]; then
    wget "https://salsa.debian.org/rlb/deb-emacs/-/archive/deb/emacs/v/30.1+1-9/deb-emacs-deb-emacs-v-30.1+1-9.tar.gz"
    tar -xvzf "deb-emacs-deb-emacs-v-30.1+1-9.tar.gz"
    rm "deb-emacs-deb-emacs-v-30.1+1-9.tar.gz"
    mv "deb-emacs-deb-emacs-v-30.1+1-9" "emacs"
    run_autogen emacs
    configure emacs --with-x-toolkit=no --with-mailutils
fi

#enscript for enscript
if [ ! -d "enscript" ]; then
    wget "https://salsa.debian.org/debian/enscript/-/archive/debian/1.6.5.90-3.1/enscript-debian-1.6.5.90-3.1.tar.gz"
    tar -xvzf "enscript-debian-1.6.5.90-3.1.tar.gz"
    rm "enscript-debian-1.6.5.90-3.1.tar.gz"
    mv "enscript-debian-1.6.5.90-3.1" "enscript"
    configure enscript
fi

#expect for expect
if [ ! -d "expect" ]; then
    wget "https://salsa.debian.org/tcltk-team/expect/-/archive/debian/5.45.4-4/expect-debian-5.45.4-4.tar.gz"
    tar -xvzf "expect-debian-5.45.4-4.tar.gz"
    rm "expect-debian-5.45.4-4.tar.gz"
    mv "expect-debian-5.45.4-4" "expect"
    configure expect
fi

#file for file
if [ ! -d "file" ]; then
    wget "https://github.com/file/file/archive/refs/tags/FILE5_46.tar.gz"
    tar -xvzf "FILE5_46.tar.gz"
    rm "FILE5_46.tar.gz"
    mv "file-FILE5_46" "file"
    run_autoreconf file
    configure file
fi

#findutils for findutils
if [ ! -d "findutils" ]; then
    wget "https://ftp.gnu.org/gnu/findutils/findutils-4.10.0.tar.xz"
    tar -xvf "findutils-4.10.0.tar.xz"
    rm "findutils-4.10.0.tar.xz"
    mv "findutils-4.10.0" "findutils"
    configure findutils
fi

#finger for finger and fingerd
if [ ! -d "finger" ]; then
    wget "https://salsa.debian.org/debian/bsd-finger/-/archive/debian/0.17-17/bsd-finger-debian-0.17-17.tar.gz"
    tar -xvzf "bsd-finger-debian-0.17-17.tar.gz"
    rm "bsd-finger-debian-0.17-17.tar.gz"
    mv "bsd-finger-debian-0.17-17" "finger"
    configure finger
fi

#fping for fping
if [ ! -d "fping" ]; then
    wget "https://github.com/schweikert/fping/releases/download/v5.4/fping-5.4.tar.gz"
    tar -xvzf "fping-5.4.tar.gz"
    rm "fping-5.4.tar.gz"
    mv "fping-5.4" "fping"
    configure fping
fi

#gawk for gawk
if [ ! -d "gawk" ]; then
    wget "https://ftp.gnu.org/gnu/gawk/gawk-5.3.2.tar.xz"
    tar -xvf "gawk-5.3.2.tar.xz"
    rm "gawk-5.3.2.tar.xz"
    mv "gawk-5.3.2" "gawk"
    configure gawk
fi

#gdb for gdb
if [ ! -d "gdb" ]; then
    wget "https://salsa.debian.org/gdb-team/gdb/-/archive/debian/17.0.90-1_exp2/gdb-debian-17.0.90-1_exp2.tar.gz"
    tar -xvzf "gdb-debian-17.0.90-1_exp2.tar.gz"
    rm "gdb-debian-17.0.90-1_exp2.tar.gz"
    mv "gdb-debian-17.0.90-1_exp2" "gdb"
    configure gdb
fi

#CPG does not build
#gcc for gcc
#if [ ! -d "gcc" ]; then
#    wget "https://github.com/gcc-mirror/gcc/archive/refs/tags/releases/gcc-15.2.0.tar.gz"
#    tar -xvzf "gcc-15.2.0.tar.gz"
#    rm "gcc-15.2.0.tar.gz"
#    mv "gcc-releases-gcc-15.2.0" "gcc"
#fi

#CPG does not build
#gettext for gettext and gettext-base
# if [ ! -d "gettext" ]; then
#     wget "https://ftp.gnu.org/pub/gnu/gettext/gettext-0.26.tar.gz"
#     tar -xvzf "gettext-0.26.tar.gz"
#     rm "gettext-0.26.tar.gz"
#     mv "gettext-0.26" "gettext"
#     configure gettext
# fi

#gimp for gimp
if [ ! -d "gimp" ]; then
    wget https://ftp.debian.org/debian/pool/main/g/gimp/gimp_2.10.22.orig.tar.bz2
    tar -xvjf "gimp_2.10.22.orig.tar.bz2"
    rm "gimp_2.10.22.orig.tar.bz2"
    mv "gimp-2.10.22" "gimp"
    configure gimp --disable-python
fi

#git for git
if [ ! -d "git" ]; then
    wget "https://github.com/git/git/archive/refs/tags/v2.51.0.tar.gz"
    tar -xvzf "v2.51.0.tar.gz"
    rm "v2.51.0.tar.gz"
    mv "git-2.51.0" "git"
    run_meson git
fi

#glib for libglib2.0-dev-bin
if [ ! -d "glib" ]; then
    wget "https://ftp.debian.org/debian/pool/main/g/glib2.0/glib2.0_2.86.1.orig.tar.xz"
    tar -xvf "glib2.0_2.86.1.orig.tar.xz"
    rm "glib2.0_2.86.1.orig.tar.xz"
    mv "glib-2.86.1" "glib"
    run_meson glib
fi

#glibc for libc-bin
if [ ! -d "glibc" ]; then
    wget "https://ftp.gnu.org/gnu/libc/glibc-2.42.tar.xz"
    tar -xvf "glibc-2.42.tar.xz"
    rm "glibc-2.42.tar.xz"
    mv "glibc-2.42" "glibc"
    if [ $CONFIGURE = true ]; then
        cd glibc
        mkdir build
        cd build
        ../configure --prefix=$HOME
        cd $SOURCE_DIR   
    fi
fi

#gnucobol for gnucobol
if [ ! -d "gnucobol" ]; then
    wget "https://ftp.debian.org/debian/pool/main/g/gnucobol3/gnucobol3_3.2.orig.tar.xz"
    tar -xvf "gnucobol3_3.2.orig.tar.xz"
    rm "gnucobol3_3.2.orig.tar.xz"
    mv "gnucobol-3.2" "gnucobol"
    run_autoreconf gnucobol
    configure gnucobol
fi

#grep for grep
if [ ! -d "grep" ]; then
    wget "https://ftp.gnu.org/gnu/grep/grep-3.12.tar.xz"
    tar -xvf "grep-3.12.tar.xz"
    rm "grep-3.12.tar.xz"
    mv "grep-3.12" "grep"
    configure grep
fi

#gzip for gzip
if [ ! -d "gzip" ]; then
    wget "https://salsa.debian.org/debian/gzip/-/archive/debian/1.14-1_exp2/gzip-debian-1.14-1_exp2.tar.gz"
    tar -xvzf "gzip-debian-1.14-1_exp2.tar.gz"
    rm "gzip-debian-1.14-1_exp2.tar.gz"
    mv "gzip-debian-1.14-1_exp2" "gzip"
    configure gzip
fi

#hostname for hostname
if [ ! -d "hostname" ]; then
    wget "https://salsa.debian.org/meskes/hostname/-/archive/debian/3.25/hostname-debian-3.25.tar.gz"
    tar -xvzf "hostname-debian-3.25.tar.gz"
    rm "hostname-debian-3.25.tar.gz"
    mv "hostname-debian-3.25" "hostname"
    #custom build
fi

#hping3 for hping3
if [ ! -d "hping3" ]; then
    wget "https://salsa.debian.org/debian/hping3/-/archive/debian/3.a2.ds2-10.1/hping3-debian-3.a2.ds2-10.1.tar.gz"
    tar -xvzf "hping3-debian-3.a2.ds2-10.1.tar.gz"
    rm "hping3-debian-3.a2.ds2-10.1.tar.gz"
    mv "hping3-debian-3.a2.ds2-10.1" "hping3"
    configure hping3
fi

#iftop for iftop
if [ ! -d "iftop" ]; then
    wget "https://salsa.debian.org/debian/iftop/-/archive/debian/1.0_pre4-9/iftop-debian-1.0_pre4-9.tar.gz"
    tar -xvzf "iftop-debian-1.0_pre4-9.tar.gz"
    rm "iftop-debian-1.0_pre4-9.tar.gz"
    mv "iftop-debian-1.0_pre4-9" "iftop"
    configure iftop
fi

#inetutils for -ftp, -ftpd, -syslogd, -talk, -talkd, -telnet, -telnetd, -traceroute
if [ ! -d "inetutils" ]; then
    wget "https://ftp.gnu.org/gnu/inetutils/inetutils-2.6.tar.xz"
    tar -xvf "inetutils-2.6.tar.xz"
    rm "inetutils-2.6.tar.xz"
    mv "inetutils-2.6" "inetutils"
    configure inetutils
fi

#iproute2 for iproute2
if [ ! -d "iproute2" ]; then
    wget "https://github.com/iproute2/iproute2/archive/refs/tags/v6.16.0.tar.gz"
    tar -xvzf "v6.16.0.tar.gz"
    rm "v6.16.0.tar.gz"
    mv "iproute2-6.16.0" "iproute2"
    configure iproute2
fi

#iputils for iputils-arping, iputils-ping, iputils-clockdiff, iputils-tracepath
if [ ! -d "iputils" ]; then
    wget "https://github.com/iputils/iputils/releases/download/20250605/iputils-20250605.tar.gz"
    tar -xvzf "iputils-20250605.tar.gz"
    rm "iputils-20250605.tar.gz"
    mv "iputils-20250605" "iputils"
    configure iputils
fi

#ispell for ispell
if [ ! -d "ispell" ]; then
    wget "https://salsa.debian.org/debian/ispell/-/archive/debian/3.4.06-1/ispell-debian-3.4.06-1.tar.gz"
    tar -xvzf "ispell-debian-3.4.06-1.tar.gz"
    rm "ispell-debian-3.4.06-1.tar.gz"
    mv "ispell-debian-3.4.06-1" "ispell"
    #custom build
    if [ $CONFIGURE = true ]; then
        cd ispell
        cp local.h.linux local.h
        cd $SOURCE_DIR   
    fi
fi

#joe for joe
if [ ! -d "joe" ]; then
    wget "https://sourceforge.net/projects/joe-editor/files/JOE%20sources/joe-4.6/joe-4.6.tar.gz"
    tar -xvzf "joe-4.6.tar.gz"
    rm "joe-4.6.tar.gz"
    mv "joe-4.6" "joe"
    configure joe
fi

#jq for jq
if [ ! -d "jq" ]; then
    wget "https://ftp.debian.org/debian/pool/main/j/jq/jq_1.8.1.orig.tar.gz"
    tar -xvzf jq_1.8.1.orig.tar.gz
    rm "jq_1.8.1.orig.tar.gz"
    mv "jq-jq-1.8.1" "jq"
    run_autoreconf jq
    configure jq
fi

#kbd for kbd
if [ ! -d "kbd" ]; then
    wget "https://github.com/legionus/kbd/releases/download/v2.9.0/kbd-2.9.0.tar.xz"
    tar -xvf "kbd-2.9.0.tar.xz"
    rm "kbd-2.9.0.tar.xz"
    mv "kbd-2.9.0" "kbd"
    configure kbd
fi

#krb5 for krb5-user
if [ ! -d "krb5" ]; then
    wget "https://salsa.debian.org/debian/krb5/-/archive/debian/1.18.3-6+deb11u7/krb5-debian-1.18.3-6+deb11u7.tar.gz"
    tar -xvzf "krb5-debian-1.18.3-6+deb11u7.tar.gz"
    rm "krb5-debian-1.18.3-6+deb11u7.tar.gz"
    mv "krb5-debian-1.18.3-6+deb11u7" "krb5"
    configure krb5/src
fi

#less for less
if [ ! -d "less" ]; then
    wget "https://ftp.gnu.org/gnu/less/less-679.tar.gz"
    tar -xvzf "less-679.tar.gz"
    rm "less-679.tar.gz"
    mv "less-679" "less"
    configure less
fi

#libcap2-bin for libcap2-bin
if [ ! -d "libcap2-bin" ]; then
    wget "https://salsa.debian.org/debian/libcap2/-/archive/debian/1%252.75-10/libcap2-debian-1%252.75-10.tar.gz"
    tar -xvzf "libcap2-debian-1%2.75-10.tar.gz"
    rm "libcap2-debian-1%2.75-10.tar.gz"
    mv "libcap2-debian-1%2.75-10" "libcap2-bin"
    #custom build
fi

#libvirt for libvirt-clients
if [ ! -d "libvirt" ]; then
    wget "https://salsa.debian.org/libvirt-team/libvirt/-/archive/debian/11.7.0-1/libvirt-debian-11.7.0-1.tar.gz"
    tar -xvzf "libvirt-debian-11.7.0-1.tar.gz"
    rm "libvirt-debian-11.7.0-1.tar.gz"
    mv "libvirt-debian-11.7.0-1" "libvirt"
    run_meson libvirt
fi

#links for links
if [ ! -d "links" ]; then
    wget "https://salsa.debian.org/debian/links2/-/archive/debian/2.29-2/links2-debian-2.29-2.tar.gz"
    tar -xvzf "links2-debian-2.29-2.tar.gz"
    rm "links2-debian-2.29-2.tar.gz"
    mv "links2-debian-2.29-2" "links"
    configure links
fi

#linux for linux-tools-common
if [ ! -d "linux" ]; then
    wget "https://launchpad.net/ubuntu/+archive/primary/+sourcefiles/linux/4.15.0-213.224/linux_4.15.0.orig.tar.gz"
    tar -xvzf "linux_4.15.0.orig.tar.gz"
    rm "linux_4.15.0.orig.tar.gz"
    mv "linux-4.15" "linux"
    #custom build
fi

#ltrace for ltrace
if [ ! -d "ltrace" ]; then
    wget "https://ftp.debian.org/debian/pool/main/l/ltrace/ltrace_0.7.3.orig.tar.bz2"
    tar -xvjf "ltrace_0.7.3.orig.tar.bz2"
    rm "ltrace_0.7.3.orig.tar.bz2"
    mv "ltrace-0.7.3" "ltrace"
    configure ltrace
fi

#lua for lua5.4
if [ ! -d "lua" ]; then
    wget "https://www.lua.org/ftp/lua-5.4.8.tar.gz"
    tar -xvzf "lua-5.4.8.tar.gz"
    rm "lua-5.4.8.tar.gz"
    mv "lua-5.4.8" "lua"
    #custom build
fi

#lvm2 for dmsetup, lvm2, lvm2-lockd
if [ ! -d "lvm2" ]; then
    wget "https://salsa.debian.org/lvm-team/lvm2/-/archive/debian/2.03.31-2/lvm2-debian-2.03.31-2.tar.gz"
    tar -xvzf "lvm2-debian-2.03.31-2.tar.gz"
    rm "lvm2-debian-2.03.31-2.tar.gz"
    mv "lvm2-debian-2.03.31-2" "lvm2"
    configure lvm2
fi

#mailutils for mailutils,  mailutils-imap4d, mailutils-comsatd, mailutils-mda, mailutils-pop3d
if [ ! -d "mailutils" ]; then
    wget "https://ftp.debian.org/debian/pool/main/m/mailutils/mailutils_3.20.orig.tar.xz"
    tar -xvf "mailutils_3.20.orig.tar.xz"
    rm "mailutils_3.20.orig.tar.xz"
    mv "mailutils-3.20" "mailutils"
    configure mailutils
fi

#make for make
if [ ! -d "make" ]; then
    wget "https://salsa.debian.org/srivasta/make-dfsg/-/archive/debian/4.3-4/make-dfsg-debian-4.3-4.tar.gz"
    tar -xvzf "make-dfsg-debian-4.3-4.tar.gz"
    rm "make-dfsg-debian-4.3-4.tar.gz"
    mv "make-dfsg-debian-4.3-4" "make"
    run_autoreconf make
    configure make
fi

#man-db for man-db
if [ ! -d "man-db" ]; then
    wget "https://salsa.debian.org/debian/man-db/-/archive/debian/2.13.1-1/man-db-debian-2.13.1-1.tar.gz"
    tar -xvzf "man-db-debian-2.13.1-1.tar.gz"
    rm "man-db-debian-2.13.1-1.tar.gz"
    mv "man-db-debian-2.13.1-1" "man-db"
    configure man-db
fi

#mawk for mawk
if [ ! -d "mawk" ]; then
    wget "https://salsa.debian.org/debian/mawk/-/archive/debian/1.3.4.20250131-1/mawk-debian-1.3.4.20250131-1.tar.gz"
    tar -xvzf "mawk-debian-1.3.4.20250131-1.tar.gz"
    rm "mawk-debian-1.3.4.20250131-1.tar.gz"
    mv "mawk-debian-1.3.4.20250131-1" "mawk"
    configure mawk
fi

#minicom for minicom
if [ ! -d "minicom" ]; then
    wget "https://salsa.debian.org/minicom-team/minicom/-/archive/2.10/minicom-2.10.tar.gz"
    tar -xvzf "minicom-2.10.tar.gz"
    rm "minicom-2.10.tar.gz"
    mv "minicom-2.10" "minicom"
    configure minicom
fi

#mksh for mksh
if [ ! -d "mksh" ]; then
    wget "https://github.com/MirBSD/mksh/archive/refs/tags/mksh-R59c.tar.gz"
    tar -xvzf "mksh-R59c.tar.gz"
    rm "mksh-R59c.tar.gz"
    mv "mksh-mksh-R59c" "mksh"
    #custom build - no config
fi

#mosquitto for mosquitto
if [ ! -d "mosquitto" ]; then
    wget "https://github.com/eclipse-mosquitto/mosquitto/archive/refs/tags/v2.0.22.tar.gz"
    tar -xvzf "v2.0.22.tar.gz"
    rm "v2.0.22.tar.gz"
    mv "mosquitto-2.0.22" "mosquitto"
    #custom build
fi

#mtr for mtr
if [ ! -d "mtr" ]; then
    wget "https://salsa.debian.org/debian/mtr/-/archive/debian/0.96-1/mtr-debian-0.96-1.tar.gz"
    tar -xvzf "mtr-debian-0.96-1.tar.gz"
    rm "mtr-debian-0.96-1.tar.gz"
    mv "mtr-debian-0.96-1" "mtr"
    run_autoreconf mtr
    configure mtr
fi

#multitime for multitime
if [ ! -d "multitime" ]; then
    wget "https://ftp.debian.org/debian/pool/main/m/multitime/multitime_1.4.orig.tar.gz"
    tar -xvzf "multitime_1.4.orig.tar.gz"
    rm "multitime_1.4.orig.tar.gz"
    mv "multitime-1.4" "multitime"
    configure multitime
fi

#nano for nano
if [ ! -d "nano" ]; then
    wget "https://salsa.debian.org/debian/nano/-/archive/debian/8.5-1/nano-debian-8.5-1.tar.gz"
    tar -xvzf "nano-debian-8.5-1.tar.gz"
    rm "nano-debian-8.5-1.tar.gz"
    mv "nano-debian-8.5-1" "nano"
    configure nano
fi

#CPG does not build
# #nasm for nasm
# if [ ! -d "nasm" ]; then
#     wget "https://ftp.debian.org/debian/pool/main/n/nasm/nasm_3.01.orig.tar.xz"
#     tar -xvf "nasm_3.01.orig.tar.xz"
#     rm "nasm_3.01.orig.tar.xz"
#     mv "nasm-3.01" "nasm"
#     configure nasm
# fi

#ncftp for ncftp
if [ ! -d "ncftp" ]; then
    wget "https://www.ncftp.com/public_ftp/ncftp/ncftp-3.3.0-src.tar.gz"
    tar -xvzf "ncftp-3.3.0-src.tar.gz"
    rm "ncftp-3.3.0-src.tar.gz"
    mv "ncftp-3.3.0" "ncftp"
    configure ncftp
fi

#ncurses for ncurses-bin
if [ ! -d "ncurses" ]; then
    wget "https://salsa.debian.org/debian/ncurses/-/archive/debian/6.5+20250216-2/ncurses-debian-6.5+20250216-2.tar.gz"
    tar -xvzf "ncurses-debian-6.5+20250216-2.tar.gz"
    rm "ncurses-debian-6.5+20250216-2.tar.gz"
    mv "ncurses-debian-6.5+20250216-2" "ncurses"
    configure ncurses
fi

#net-tools for net-tools
if [ ! -d "net-tools" ]; then
    wget "https://ftp.debian.org/debian/pool/main/n/net-tools/net-tools_2.10.orig.tar.xz"
    tar -xvf "net-tools_2.10.orig.tar.xz"
    rm "net-tools_2.10.orig.tar.xz"
    mv "net-tools-2.10" "net-tools"
    if [ $CONFIGURE = true ]; then
        cd net-tools
        make config    
    fi
fi

#netcat-bsd for netcat-openbsd
if [ ! -d "netcat-bsd" ]; then
    wget "https://salsa.debian.org/debian/netcat-openbsd/-/archive/debian/1.229-1/netcat-openbsd-debian-1.229-1.tar.gz"
    tar -xvzf "netcat-openbsd-debian-1.229-1.tar.gz"
    rm "netcat-openbsd-debian-1.229-1.tar.gz"
    mv "netcat-openbsd-debian-1.229-1" "netcat-bsd"
    #custom build - no config
fi

#netcat-traditional for netcat-traditional
if [ ! -d "netcat-traditional" ]; then
    wget "https://salsa.debian.org/debian/netcat/-/archive/debian/1.10-48.2/netcat-debian-1.10-48.2.tar.gz"
    tar -xvzf "netcat-debian-1.10-48.2.tar.gz"
    rm "netcat-debian-1.10-48.2.tar.gz"
    mv "netcat-debian-1.10-48.2" "netcat-traditional"
    #custom build - variable in CPG creation
fi

#netkit-rsh for rsh-client
if [ ! -d "netkit-rsh" ]; then
    wget "https://launchpad.net/ubuntu/+archive/primary/+sourcefiles/netkit-rsh/0.17-17ubuntu0.1/netkit-rsh_0.17.orig.tar.gz"
    tar -xvzf "netkit-rsh_0.17.orig.tar.gz"
    rm "netkit-rsh_0.17.orig.tar.gz"
    mv "netkit-rsh-0.17" "netkit-rsh"
    configure netkit-rsh
fi

#nftables for nftables
if [ ! -d "nftables" ]; then
    wget "https://salsa.debian.org/pkg-netfilter-team/pkg-nftables/-/archive/debian/0.9.8-3.1/pkg-nftables-debian-0.9.8-3.1.tar.gz"
    tar -xvzf "pkg-nftables-debian-0.9.8-3.1.tar.gz"
    rm "pkg-nftables-debian-0.9.8-3.1.tar.gz"
    mv "pkg-nftables-debian-0.9.8-3.1" "nftables"
    configure nftables
fi

#ntp for ntp, ntpdate
if [ ! -d "ntp" ]; then
    wget "https://downloads.nwtime.org/ntp/4.2.8/ntp-4.2.8.tar.gz"
    tar -xvzf "ntp-4.2.8.tar.gz"
    rm "ntp-4.2.8.tar.gz"
    mv "ntp-4.2.8" "ntp"
    configure ntp
fi

#openssh for openssh-client
if [ ! -d "openssh" ]; then
    wget "https://salsa.debian.org/ssh-team/openssh/-/archive/debian/1%2510.0p1-8/openssh-debian-1%2510.0p1-8.tar.gz"
    tar -xvzf "openssh-debian-1%10.0p1-8.tar.gz"
    rm "openssh-debian-1%10.0p1-8.tar.gz"
    mv "openssh-debian-1%10.0p1-8" "openssh"
    configure openssh
fi

#openssl for openssl
if [ ! -d "openssl" ]; then
    wget "https://github.com/openssl/openssl/releases/download/openssl-3.5.2/openssl-3.5.2.tar.gz"
    tar -xvzf "openssl-3.5.2.tar.gz"
    rm "openssl-3.5.2.tar.gz"
    mv "openssl-3.5.2" "openssl"
    if [ $CONFIGURE = true ]; then
        cd openssl
        ./Configure
        cd $SOURCE_DIR
    fi
fi

#openvpn for openvpn
if [ ! -d "openvpn" ]; then
    wget "https://github.com/OpenVPN/openvpn/releases/download/v2.6.14/openvpn-2.6.14.tar.gz"
    tar -xvzf "openvpn-2.6.14.tar.gz"
    rm "openvpn-2.6.14.tar.gz"
    mv "openvpn-2.6.14" "openvpn"
    configure openvpn
fi

#openwince-jtag for jtag
if [ ! -d "openwince-jtag" ]; then
    wget "https://salsa.debian.org/debian/openwince-jtag/-/archive/debian/0.5.1-11/openwince-jtag-debian-0.5.1-11.tar.gz"
    tar -xvzf "openwince-jtag-debian-0.5.1-11.tar.gz"
    rm "openwince-jtag-debian-0.5.1-11.tar.gz"
    mv "openwince-jtag-debian-0.5.1-11" "openwince-jtag"
    configure openwince-jtag
fi

#opkg for _opkg
if [ ! -d "opkg" ]; then
    wget "https://github.com/oe-mirrors/opkg/archive/refs/tags/v0.6.1.tar.gz"
    tar -xvzf "v0.6.1.tar.gz"
    rm "v0.6.1.tar.gz"
    mv "opkg-0.6.1" "opkg"
    run_autoreconf opkg
    configure opkg
fi

#original-awk for original-awk
if [ ! -d "original-awk" ]; then
    wget "https://github.com/onetrueawk/awk/archive/refs/tags/20250116.tar.gz"
    tar -xvzf "20250116.tar.gz"
    rm "20250116.tar.gz"
    mv "awk-20250116" "original-awk"
    #custom build - no config
fi

#pax for pax
if [ ! -d "pax" ]; then
    wget "https://github.com/MirBSD/mircpio/archive/refs/tags/paxmirabilis-20240817.tar.gz"
    tar -xvzf "paxmirabilis-20240817.tar.gz"
    rm "paxmirabilis-20240817.tar.gz"
    mv "mircpio-paxmirabilis-20240817" "pax"
    #custom build - no config
fi

#pexec for pexec
if [ ! -d "pexec" ]; then
    wget "http://ftp.gnu.org/gnu/pexec/pexec-1.0rc8.tar.gz"
    tar -xvzf "pexec-1.0rc8.tar.gz"
    rm "pexec-1.0rc8.tar.gz"
    mv "pexec-1.0rc8" "pexec"
    configure pexec
fi

#php for php-cli
if [ ! -d "php" ]; then
    wget "https://github.com/php/php-src/archive/refs/tags/php-8.4.12.tar.gz"
    tar -xvzf "php-8.4.12.tar.gz"
    rm "php-8.4.12.tar.gz"
    mv "php-src-php-8.4.12" "php"
    autoreconf php
    configure php
fi

#pkg for _pkg
if [ ! -d "pkg" ]; then
    wget "https://github.com/freebsd/pkg/archive/refs/tags/2.3.1.tar.gz"
    tar -xvzf "2.3.1.tar.gz"
    rm "2.3.1.tar.gz"
    mv "pkg-2.3.1" "pkg"
    configure pkg
fi

#polkit for pkexec and polkitd
if [ ! -d "polkit" ]; then
    wget "https://salsa.debian.org/utopia-team/polkit/-/archive/debian/126-2/polkit-debian-126-2.tar.gz"
    tar -xvzf "polkit-debian-126-2.tar.gz"
    rm "polkit-debian-126-2.tar.gz"
    mv "polkit-debian-126-2" "polkit"
    run_meson polkit
fi

#posh for posh
if [ ! -d "posh" ]; then
    wget "https://salsa.debian.org/clint/posh/-/archive/debian/0.14.1/posh-debian-0.14.1.tar.gz"
    tar -xvzf "posh-debian-0.14.1.tar.gz"
    rm "posh-debian-0.14.1.tar.gz"
    mv "posh-debian-0.14.1" "posh"
    run_autoreconf posh
    configure posh
fi

#postgres for postgresql-client-common
if [ ! -d "postgres" ]; then
    wget "https://github.com/postgres/postgres/archive/refs/tags/REL_18_RC1.tar.gz"
    tar -xvzf "REL_18_RC1.tar.gz"
    rm "REL_18_RC1.tar.gz"
    mv "postgres-REL_18_RC1" "postgres"
    configure postgres
fi

#procps for procps
if [ ! -d "procps" ]; then
    wget "https://ftp.debian.org/debian/pool/main/p/procps/procps_4.0.4.orig.tar.xz"
    tar -xvf "procps_4.0.4.orig.tar.xz"
    rm "procps_4.0.4.orig.tar.xz"
    mv "procps-ng-4.0.4" "procps"
    configure procps
fi

#putty for putty-tools
if [ ! -d "putty" ]; then
    wget "https://salsa.debian.org/ssh-team/putty/-/archive/debian/0.83-3/putty-debian-0.83-3.tar.gz"
    tar -xvzf "putty-debian-0.83-3.tar.gz"
    rm "putty-debian-0.83-3.tar.gz"
    mv "putty-debian-0.83-3" "putty"
    run_cmake putty
fi

#rc for rc
if [ ! -d "rc" ]; then
    mkdir rc
    wget "https://ftp.debian.org/debian/pool/main/r/rc/rc_1.7.4+97.gceb59bb.orig.tar.xz"
    tar -xvf "rc_1.7.4+97.gceb59bb.orig.tar.xz" -C rc
    rm "rc_1.7.4+97.gceb59bb.orig.tar.xz"
    if [ $CONFIGURE = true ]; then
        cd rc
        ./bootstrap
        cd $SOURCE_DIR
        configure rc
    fi
fi

#redis for _redis
if [ ! -d "redis" ]; then
    wget "https://github.com/redis/redis/archive/refs/tags/8.2.1.tar.gz"
    tar -xvzf "8.2.1.tar.gz"
    rm "8.2.1.tar.gz"
    mv "redis-8.2.1" "redis"
    #custom build
fi

#rlwrap for rlwrap
if [ ! -d "rlwrap" ]; then
    wget "https://salsa.debian.org/debian/rlwrap/-/archive/debian/0.46.1-1/rlwrap-debian-0.46.1-1.tar.gz"
    tar -xvzf "rlwrap-debian-0.46.1-1.tar.gz"
    rm "rlwrap-debian-0.46.1-1.tar.gz"
    mv "rlwrap-debian-0.46.1-1" "rlwrap"
    run_autoreconf rlwrap
    configure rlwrap
fi

#rpm for rpm
if [ ! -d "rpm" ]; then
    wget "https://salsa.debian.org/pkg-rpm-team/rpm/-/archive/debian/4.20.1+dfsg-3/rpm-debian-4.20.1+dfsg-3.tar.gz"
    tar -xvzf "rpm-debian-4.20.1+dfsg-3.tar.gz"
    rm "rpm-debian-4.20.1+dfsg-3.tar.gz"
    mv "rpm-debian-4.20.1+dfsg-3" "rpm"
    run_cmake rpm -DWITH_SEQUOIA=OFF
fi

#rsync for rsync
if [ ! -d "rsync" ]; then
    wget "https://github.com/RsyncProject/rsync/releases/download/v3.4.1/rsync-3.4.1.tar.gz"
    tar -xvzf "rsync-3.4.1.tar.gz"
    rm "rsync-3.4.1.tar.gz"
    mv "rsync-3.4.1" "rsync"
    configure rsync
fi

#ruby for ruby
if [ ! -d "ruby" ]; then
    wget "https://github.com/ruby/ruby/archive/refs/tags/v3_4_5.tar.gz"
    tar -xvzf "v3_4_5.tar.gz"
    rm "v3_4_5.tar.gz"
    mv "ruby-3_4_5" "ruby"
    run_autogen ruby
    configure ruby
fi

#samba for smbclient
if [ ! -d "samba" ]; then
    wget "https://github.com/samba-team/samba/archive/refs/tags/samba-4.23.0.tar.gz"
    tar -xvzf "samba-4.23.0.tar.gz"
    rm "samba-4.23.0.tar.gz"
    mv "samba-samba-4.23.0" "samba"
    configure samba
fi

#sash for sash
if [ ! -d "sash" ]; then
    wget "https://ftp.debian.org/debian/pool/main/s/sash/sash_3.8.orig.tar.gz"
    tar -xvzf "sash_3.8.orig.tar.gz"
    rm "sash_3.8.orig.tar.gz"
    mv "sash-3.8" "sash"
    #custom build - variables in CPG creation
fi

#scanmem for scanmem
if [ ! -d "scanmem" ]; then
    wget "https://ftp.debian.org/debian/pool/main/s/scanmem/scanmem_0.17.orig.tar.gz"
    tar -xvzf "scanmem_0.17.orig.tar.gz"
    rm "scanmem_0.17.orig.tar.gz"
    mv "scanmem-0.17" "scanmem"
    run_autogen scanmem
    configure scanmem
fi

#screen for screen
if [ ! -d "screen" ]; then
    wget "https://salsa.debian.org/debian/screen/-/archive/debian/4.9.1-3/screen-debian-4.9.1-3.tar.gz"
    tar -xvzf "screen-debian-4.9.1-3.tar.gz"
    rm "screen-debian-4.9.1-3.tar.gz"
    mv "screen-debian-4.9.1-3" "screen"
    configure screen
fi

#scrot for scrot
if [ ! -d "scrot" ]; then
    wget "https://salsa.debian.org/debian/scrot/-/archive/debian/1.12.1-1/scrot-debian-1.12.1-1.tar.gz"
    tar -xvzf "scrot-debian-1.12.1-1.tar.gz"
    rm "scrot-debian-1.12.1-1.tar.gz"
    mv "scrot-debian-1.12.1-1" "scrot"
    run_autogen scrot
    configure scrot
fi

#sed for sed
if [ ! -d "sed" ]; then
    wget "https://ftp.gnu.org/gnu/sed/sed-4.9.tar.xz"
    tar -xvf "sed-4.9.tar.xz"
    rm "sed-4.9.tar.xz"
    mv "sed-4.9" "sed"
    configure sed
fi

#shadow-utils for passwd, uidmap
if [ ! -d "shadow-utils" ]; then
    wget "https://github.com/shadow-maint/shadow/releases/download/4.18.0/shadow-4.18.0.tar.gz"
    tar -xvzf "shadow-4.18.0.tar.gz"
    rm "shadow-4.18.0.tar.gz"
    mv "shadow-4.18.0" "shadow-utils"
    configure shadow-utils
fi

#sharutils for sharutils
if [ ! -d "sharutils" ]; then
    wget "https://ftp.gnu.org/gnu/sharutils/sharutils-4.15.tar.xz"
    tar -xvf "sharutils-4.15.tar.xz"
    rm "sharutils-4.15.tar.xz"
    mv "sharutils-4.15" "sharutils"
    configure sharutils
fi

#slang2 for slsh
if [ ! -d "slang2" ]; then
    wget "https://salsa.debian.org/debian/slang2/-/archive/debian/2.3.3-5/slang2-debian-2.3.3-5.tar.gz"
    tar -xvzf "slang2-debian-2.3.3-5.tar.gz"
    rm "slang2-debian-2.3.3-5.tar.gz"
    mv "slang2-debian-2.3.3-5" "slang2"
    configure slang2
fi

#socat for socat
if [ ! -d "socat" ]; then
    wget "https://ftp.debian.org/debian/pool/main/s/socat/socat_1.8.0.3.orig.tar.bz2"
    tar -xvjf "socat_1.8.0.3.orig.tar.bz2"
    rm "socat_1.8.0.3.orig.tar.bz2"
    mv "socat-1.8.0.3" "socat"
    configure socat
fi

#squashfs-tools for squashfs-tools
if [ ! -d "squashfs-tools" ]; then
    wget "https://github.com/plougher/squashfs-tools/releases/download/4.7.2/squashfs-tools-4.7.2.tar.gz"
    tar -xvzf "squashfs-tools-4.7.2.tar.gz"
    rm "squashfs-tools-4.7.2.tar.gz"
    mv "squashfs-tools-4.7.2" "squashfs-tools"
    #custom build
fi

#sshpass for sshpass
if [ ! -d "sshpass" ]; then
    wget "https://sourceforge.net/projects/sshpass/files/sshpass/1.10/sshpass-1.10.tar.gz"
    tar -xvzf "sshpass-1.10.tar.gz"
    rm "sshpass-1.10.tar.gz"
    mv "sshpass-1.10" "sshpass"
    configure sshpass
fi

#strace for strace
if [ ! -d "strace" ]; then
    wget "https://github.com/strace/strace/releases/download/v6.16/strace-6.16.tar.xz"
    tar -xvf "strace-6.16.tar.xz"
    rm "strace-6.16.tar.xz"
    mv "strace-6.16" "strace"
    configure strace --enable-mpers=no
fi

#sudo for sudo
if [ ! -d "sudo" ]; then
    wget "https://salsa.debian.org/sudo-team/sudo/-/archive/debian/1.9.17p2-1/sudo-debian-1.9.17p2-1.tar.gz"
    tar -xvzf "sudo-debian-1.9.17p2-1.tar.gz"
    rm "sudo-debian-1.9.17p2-1.tar.gz"
    mv "sudo-debian-1.9.17p2-1" "sudo"
    configure sudo
fi

#sysstat for sysstat
if [ ! -d "sysstat" ]; then
    wget "https://salsa.debian.org/debian/sysstat/-/archive/debian/12.7.5-2/sysstat-debian-12.7.5-2.tar.gz"
    tar -xvzf "sysstat-debian-12.7.5-2.tar.gz"
    rm "sysstat-debian-12.7.5-2.tar.gz"
    mv "sysstat-debian-12.7.5-2" "sysstat"
    configure sysstat
fi

#systemd for systemd
if [ ! -d "systemd" ]; then
    wget "https://github.com/systemd/systemd/archive/refs/tags/v257.9.tar.gz"
    tar -xvzf "v257.9.tar.gz"
    rm "v257.9.tar.gz"
    mv "systemd-257.9" "systemd"
    run_meson systemd
fi

#tar for tar
if [ ! -d "tar" ]; then
    wget "https://ftp.gnu.org/gnu/tar/tar-1.35.tar.xz"
    tar -xvf "tar-1.35.tar.xz"
    rm "tar-1.35.tar.xz"
    mv "tar-1.35" "tar"
    configure tar
fi

#tcpdump for tcpdump
if [ ! -d "tcpdump" ]; then
    wget "https://github.com/the-tcpdump-group/tcpdump/archive/refs/tags/tcpdump-4.99.5.tar.gz"
    tar -xvzf "tcpdump-4.99.5.tar.gz"
    rm "tcpdump-4.99.5.tar.gz"
    mv "tcpdump-tcpdump-4.99.5" "tcpdump"
    run_autogen tcpdump
    configure tcpdump
fi

#tdb-tools for tdb-tools
if [ ! -d "tdb-tools" ]; then
    wget "https://salsa.debian.org/samba-team/tdb/-/archive/debian/1.4.12-1_bpo12+1/tdb-debian-1.4.12-1_bpo12+1.tar.gz"
    tar -xvzf "tdb-debian-1.4.12-1_bpo12+1.tar.gz"
    rm "tdb-debian-1.4.12-1_bpo12+1.tar.gz"
    mv "tdb-debian-1.4.12-1_bpo12+1" "tdb-tools"
    configure tdb-tools
fi

#texlive-bin for texlive-binaries
if [ ! -d "texlive-bin" ]; then
    wget "https://github.com/debian-tex/texlive-bin/archive/refs/tags/debian/2025.20250727.75242+ds-4.tar.gz"
    tar -xvzf "2025.20250727.75242+ds-4.tar.gz"
    rm "2025.20250727.75242+ds-4.tar.gz"
    mv "texlive-bin-debian-2025.20250727.75242-ds-4" "texlive-bin"
    if [ $CONFIGURE = true ]; then
        cd texlive-bin
        mkdir build
        cd build
        ../configure --prefix=$HOME -disable-native-texlive-build
        cd $SOURCE_DIR
    fi
fi

#tftp-hpa for tftp-hpa
if [ ! -d "tftp-hpa" ]; then
    wget "https://salsa.debian.org/ron/tftp-hpa/-/archive/v5.2+20150808-1/tftp-hpa-v5.2+20150808-1.tar.gz"
    tar -xvzf "tftp-hpa-v5.2+20150808-1.tar.gz"
    rm "tftp-hpa-v5.2+20150808-1.tar.gz"
    mv "tftp-hpa-v5.2+20150808-1" "tftp-hpa"
    run_autogen tftp-hpa
    configure tftp-hpa
fi


#time for time
if [ ! -d "time" ]; then
    wget "https://ftp.gnu.org/gnu/time/time-1.9.tar.gz"
    tar -xvzf "time-1.9.tar.gz"
    rm "time-1.9.tar.gz"
    mv "time-1.9" "time"
    configure time
fi

#tk for tk
if [ ! -d "tk" ]; then
    wget "https://github.com/tcltk/tk/archive/refs/tags/core-8-6-17.tar.gz"
    tar -xvzf "core-8-6-17.tar.gz"
    rm "core-8-6-17.tar.gz"
    mv "tk-core-8-6-17" "tk"
    configure tk/unix
fi

#tmate for tmate
if [ ! -d "tmate" ]; then
    wget "https://salsa.debian.org/debian/tmate/-/archive/debian/2.4.0-3/tmate-debian-2.4.0-3.tar.gz"
    tar -xvzf "tmate-debian-2.4.0-3.tar.gz"
    rm "tmate-debian-2.4.0-3.tar.gz"
    mv "tmate-debian-2.4.0-3" "tmate"
    run_autogen tmate
    configure tmate
fi

#tmux for tmux
if [ ! -d "tmux" ]; then
    wget "https://salsa.debian.org/debian/tmux/-/archive/debian/3.5a-3/tmux-debian-3.5a-3.tar.gz"
    tar -xvzf "tmux-debian-3.5a-3.tar.gz"
    rm "tmux-debian-3.5a-3.tar.gz"
    mv "tmux-debian-3.5a-3" "tmux"
    configure tmux
fi

#tor for tor
if [ ! -d "tor" ]; then
    wget "https://gitlab.torproject.org/tpo/core/debian/tor/-/archive/debian-tor-0.4.8.18-1/tor-debian-tor-0.4.8.18-1.tar.gz"
    tar -xvzf "tor-debian-tor-0.4.8.18-1.tar.gz"
    rm "tor-debian-tor-0.4.8.18-1.tar.gz"
    mv "tor-debian-tor-0.4.8.18-1" "tor"
    run_autogen tor
    configure tor --disable-asciidoc
fi

#unzip for unzip
if [ ! -d "unzip" ]; then
    wget "https://salsa.debian.org/sanvila/unzip/-/archive/debian/6.0-29/unzip-debian-6.0-29.tar.gz"
    tar -xvzf "unzip-debian-6.0-29.tar.gz"
    rm "unzip-debian-6.0-29.tar.gz"
    mv "unzip-debian-6.0-29" "unzip"
    #custom build - no config
fi

#util-linux for util-linux
if [ ! -d "util-linux" ]; then
    wget "https://ftp.debian.org/debian/pool/main/u/util-linux/util-linux_2.41.2.orig.tar.xz"
    tar -xvf "util-linux_2.41.2.orig.tar.xz"
    rm "util-linux_2.41.2.orig.tar.xz"
    mv "util-linux-2.41.2" "util-linux"
    configure util-linux
fi

#valgrind for valgrind
if [ ! -d "valgrind" ]; then
    wget "https://sourceware.org/pub/valgrind/valgrind-3.25.1.tar.bz2"
    tar -xvjf "valgrind-3.25.1.tar.bz2"
    rm "valgrind-3.25.1.tar.bz2"
    mv "valgrind-3.25.1" "valgrind"
    configure valgrind
fi

#varnish for varnish
if [ ! -d "varnish" ]; then
    wget "https://salsa.debian.org/varnish-team/varnish/-/archive/debian/7.7.3-1/varnish-debian-7.7.3-1.tar.gz"
    tar -xvzf "varnish-debian-7.7.3-1.tar.gz"
    rm "varnish-debian-7.7.3-1.tar.gz"
    mv "varnish-debian-7.7.3-1" "varnish"
    run_autogen varnish
    configure varnish
fi

#vim for vim, xxd
if [ ! -d "vim" ]; then
    wget "https://salsa.debian.org/vim-team/vim/-/archive/v9.1.1385/vim-v9.1.1385.tar.gz"
    tar -xvzf "vim-v9.1.1385.tar.gz"
    rm "vim-v9.1.1385.tar.gz"
    mv "vim-v9.1.1385" "vim"
    configure vim
fi

#w3m for w3m
if [ ! -d "w3m" ]; then
    wget "https://salsa.debian.org/debian/w3m/-/archive/v0.5.3+git20210102+deb11u1/w3m-v0.5.3+git20210102+deb11u1.tar.gz"
    tar -xvzf "w3m-v0.5.3+git20210102+deb11u1.tar.gz"
    rm "w3m-v0.5.3+git20210102+deb11u1.tar.gz"
    mv "w3m-v0.5.3+git20210102+deb11u1" "w3m"
    configure w3m
fi

#wget for wget
if [ ! -d "wget" ]; then
    wget "https://ftp.gnu.org/gnu/wget/wget-1.25.0.tar.gz"
    tar -xvzf "wget-1.25.0.tar.gz"
    rm "wget-1.25.0.tar.gz"
    mv "wget-1.25.0" "wget"
    configure wget
fi

#whiptail for whiptail
if [ ! -d "whiptail" ]; then
    wget "https://salsa.debian.org/mckinstry/newt/-/archive/debian/0.52.25-1/newt-debian-0.52.25-1.tar.gz"
    tar -xvzf "newt-debian-0.52.25-1.tar.gz"
    rm "newt-debian-0.52.25-1.tar.gz"
    mv "newt-debian-0.52.25-1" "whiptail"
    configure whiptail
fi

#whois for whois
if [ ! -d "whois" ]; then
    wget "https://github.com/rfc1036/whois/archive/refs/tags/v5.6.4.tar.gz"
    tar -xvzf "v5.6.4.tar.gz"
    rm "v5.6.4.tar.gz"
    mv "whois-5.6.4" "whois"
    #custom build
fi

#wireshark for tshark and wireshark-common
if [ ! -d "wireshark" ]; then
    wget "https://github.com/wireshark/wireshark/archive/refs/tags/wireshark-4.4.9.tar.gz"
    tar -xvzf "wireshark-4.4.9.tar.gz"
    rm "wireshark-4.4.9.tar.gz"
    mv "wireshark-wireshark-4.4.9" "wireshark"
    run_cmake wireshark
fi

#x11-apps for x11-apps
if [ ! -d "x11-apps" ]; then
    wget "https://ftp.debian.org/debian/pool/main/x/x11-apps/x11-apps_7.7+11.tar.gz"
    tar -xvzf "x11-apps_7.7+11.tar.gz"
    rm "x11-apps_7.7+11.tar.gz"
    mv "x11-apps.git" "x11-apps"
    configure x11-apps/bitmap
    configure x11-apps/ico
    configure x11-apps/oclock
    configure x11-apps/transset
    configure x11-apps/x11perf
    configure x11-apps/xbiff
    configure x11-apps/xcalc
    configure x11-apps/xclipboard
    configure x11-apps/xclock
    configure x11-apps/xconsole
    configure x11-apps/xcursorgen
    configure x11-apps/xditview
    configure x11-apps/xedit
    configure x11-apps/xeyes
    configure x11-apps/xgc
    configure x11-apps/xload
    configure x11-apps/xlogo
    configure x11-apps/xmag
    configure x11-apps/xman
    configure x11-apps/xmore
    configure x11-apps/xwd
    configure x11-apps/xwud
fi

#x11-xserver-utils for x11-xserver-utils
if [ ! -d "x11-xserver-utils" ]; then
    wget "https://ftp.debian.org/debian/pool/main/x/x11-xserver-utils/x11-xserver-utils_7.7+11.tar.xz"
    tar -xvf "x11-xserver-utils_7.7+11.tar.xz"
    rm "x11-xserver-utils_7.7+11.tar.xz"
    mv "x11-xserver-utils.git" "x11-xserver-utils"
    configure x11-xserver-utils/iceauth
    configure x11-xserver-utils/rgb
    configure x11-xserver-utils/sessreg
    configure x11-xserver-utils/xcmsdb
    configure x11-xserver-utils/xgamma
    configure x11-xserver-utils/xhost
    configure x11-xserver-utils/xmodmap
    configure x11-xserver-utils/xrandr
    configure x11-xserver-utils/xrdb
    configure x11-xserver-utils/xrefresh
    configure x11-xserver-utils/xset
    configure x11-xserver-utils/xsetmode
    configure x11-xserver-utils/xsetpointer
    configure x11-xserver-utils/xsetroot
    configure x11-xserver-utils/xstdcmap
    configure x11-xserver-utils/xvidtune
fi

#xdotool for xdotool
if [ ! -d "xdotool" ]; then
    wget "https://github.com/jordansissel/xdotool/releases/download/v3.20211022.1/xdotool-3.20211022.1.tar.gz"
    tar -xvzf "xdotool-3.20211022.1.tar.gz"
    rm "xdotool-3.20211022.1.tar.gz"
    mv "xdotool-3.20211022.1" "xdotool"
    #custom build
fi

#xpad for xpad
if [ ! -d "xpad" ]; then
    wget "https://salsa.debian.org/jcfp/xpad/-/archive/debian/5.8.0-2/xpad-debian-5.8.0-2.tar.gz"
    tar -xvzf "xpad-debian-5.8.0-2.tar.gz"
    rm "xpad-debian-5.8.0-2.tar.gz"
    mv "xpad-debian-5.8.0-2" "xpad"
    run_autogen xpad
    configure xpad
fi

#xz for xz-utils
if [ ! -d "xz" ]; then
    wget "https://github.com/tukaani-project/xz/releases/download/v5.8.1/xz-5.8.1.tar.gz"
    tar -xvzf "xz-5.8.1.tar.gz"
    rm "xz-5.8.1.tar.gz"
    mv "xz-5.8.1" "xz"
    configure xz
fi

#yash for yash
if [ ! -d "yash" ]; then
    wget "https://salsa.debian.org/debian/yash/-/archive/debian/2.55-2/yash-debian-2.55-2.tar.gz"
    tar -xvzf "yash-debian-2.55-2.tar.gz"
    rm "yash-debian-2.55-2.tar.gz"
    mv "yash-debian-2.55-2" "yash"
    configure yash
fi

#yelp for yelp
if [ ! -d "yelp" ]; then
    wget "https://ftp.debian.org/debian/pool/main/y/yelp/yelp_42.2.orig.tar.xz"
    tar -xvf "yelp_42.2.orig.tar.xz"
    rm "yelp_42.2.orig.tar.xz"
    mv "yelp-42.2" "yelp"
    configure yelp
fi

#zathura for zathura
if [ ! -d "zathura" ]; then
    wget "https://ftp.debian.org/debian/pool/main/z/zathura/zathura_0.5.11.orig.tar.xz"
    tar -xvf "zathura_0.5.11.orig.tar.xz"
    rm "zathura_0.5.11.orig.tar.xz"
    mv "zathura-0.5.11" "zathura"
    run_meson zathura
fi

#zip for zip
if [ ! -d "zip" ]; then
    wget "https://github.com/r-lib/zip/archive/refs/tags/v2.3.3.tar.gz"
    tar -xvzf "v2.3.3.tar.gz"
    rm "v2.3.3.tar.gz"
    mv "zip-2.3.3" "zip"
    #custom build
fi

#zsh for zsh
if [ ! -d "zsh" ]; then
    wget "https://github.com/zsh-users/zsh/archive/refs/tags/zsh-5.9.tar.gz"
    tar -xvzf "zsh-5.9.tar.gz"
    rm "zsh-5.9.tar.gz"
    mv "zsh-zsh-5.9" "zsh"
    if [ $CONFIGURE = true ]; then
        cd zsh
        ./Util/preconfig
        cd $SOURCE_DIR
        configure zsh
    fi
fi
