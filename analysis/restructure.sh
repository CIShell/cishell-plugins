#!/bin/bash

shopt -s extglob
for f in */ 
do
  if [[ -d $f ]] 
    then
    cd $f
      srcCount=$(find -maxdepth 1 -name "src" -type d | wc -l)
      if [[ $srcCount -eq 1 ]]
      then
        sed -i 's/<packaging>eclipse-plugin<\/packaging>//' pom.xml
        mkdir -p src/main/java
        mkdir -p src/main/resources
        mv src/* src/main/java
        metaCount=$(find -maxdepth 1 -name "META-INF" -type d | wc -l)
        if [[ $metaCount -eq 1 ]]
        then
          mv META-INF src/main/resources
        fi
        osgiCount=$(find -maxdepth 1 -name "OSGI-INF" -type d | wc -l)
        if [[ $osgiCount -eq 1 ]]
        then
          mv OSGI-INF src/main/resources
        fi  
      fi 
    # for f1 in */
    # do
    #   if [[ -d $f1 ]] 
    #   then
    #     echo $f1

    #     if [[ $f1 = "target/" ]]
    #       then
    #         mkdir -p src/main/java
    #         mkdir -p src/main/resources
    #         mv src/* src/main/java
    #         mv 
    #       break
    #     fi
    #   fi   
    # done
    cd ..
  fi
done


