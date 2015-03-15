#!/bin/sh

#
# unify sql execute script
# bondshi, 2009/06/15
#

# mysql command
mysql="mysql -uroot -ptswroot147 -h115.29.191.8"
begdate=""
enddate=""


die()
{
    echo "$1"
    exit -1
}

function exec_sql_xxy()
{
    i=0
    sqlfile="$1"
    while [ $i -lt 100 ];
    do
	dbNo=$(echo "$i" | awk '{printf("%02d", $1);}')	
	j=0;
	while [ $j -lt 10 ];
	do
	    echo "$i.$j..."
	    sed "s/\$XX/$dbNo/g" $sqlfile | sed "s/\$Y/$j/g" | ${mysql}
	    if [ $? -ne 0 ]; then
		die "exec $sqlfile failed!!!"
	    fi
	    j=$(expr $j + 1)
	done
	i=$(expr $i + 1)
    done
}

function check_ymd_arg()
{
    if [ "$begdate" = "" -o "$enddate" = "" ]; then
	die "invalid 'begdate'/'enddate' parameter, should be yyyy-mm-dd";
    fi

    m=$(expr $begdate : "[0-9]\{4\}-[0-9]\{2\}-[0-9]\{2\}")
    if [ $m -ne 10 ]; then die "invalid 'begdate' parameter, should be yyyy-mm-dd"; fi

    m=$(expr $enddate : "[0-9]\{4\}-[0-9]\{2\}-[0-9]\{2\}")
    if [ $m -ne 10 ]; then die "invalid 'enddate' parameter, should be yyyy-mm-dd"; fi
        
    date1=$(date -d"$begdate" +"%Y%m%d")
    date2=$(date -d"$enddate" +"%Y%m%d")
    if [ $date1 -ge $date2 ]; then echo "begdate must be less than enddate!!!"; fi
}

function exec_sql_ymd()
{
    #vars: YYYY, YY, MM, DD
    sqlfile="$1"
    unit=""

    tabname="${sqlfile##*/}"
    if [ $(expr "$tabname" : ".*DD.*") -ne 0 ]; then
	unit="day"
    elif [ $(expr "$tabname" : ".*MM.*") -ne 0 ]; then
	unit="month"
    elif [ $(expr "$tabname" : ".*YY.*") -ne 0 ]; then
	unit="year"
    else
	die "invalid sqlfile name, should be like db.table.YYYY.MM.sql!!!"
    fi

    date1=$(date -d"$begdate" +"%Y%m%d")
    date2=$(date -d"$enddate" +"%Y%m%d")

    while [ $date1 -lt $date2 ]
      do
      echo "$date1..."
      YYYY=$(date -d"$date1" +"%Y")
      YY=$(date -d"$date1" +"%y")
      MM=$(date -d"$date1" +"%m")
      DD=$(date -d"$date1" +"%d")

      sed "s/\$YYYY/$YYYY/g" $sqlfile | sed "s/\$YY/$YY/g" | sed "s/\$MM/$MM/g" | sed "s/\$DD/$DD/g" | ${mysql}
      date1=$(date -d"1 $unit $date1" +"%Y%m%d")
    done
}

function exec_sql_single()
{
    sqlfile="$1"
    cat $sqlfile | ${mysql}

    if [ $? -ne 0 ]; then
	echo "exec $sqlfile failed!!!"
	exit -1
    fi
}

if [ $# -eq 0 ];
    then
    echo "usage:./exec_sql.sh sqlfile {begdate enddate}"
    exit -1
fi

sqlfile="$1"
tblname=${sqlfile##*/}

if [ $(expr match $tblname ".*\.XX\.Y") -ne 0 ]; then
    # XX.Y mode
    exec_sql_xxy "$sqlfile"
elif [ ! -z $(expr match $tblname ".*\(YY\|MM\|DD\).*") ]; then
    # YMD mode
    begdate="$2"
    enddate="$3"

    check_ymd_arg
    exec_sql_ymd "$sqlfile"
else
    # single table
    exec_sql_single "$sqlfile"
fi
