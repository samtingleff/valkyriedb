# ValkyrieDB #

## About ##

ValkyrieDB is a ridiculously simple read-only key/value database designed to be fed from Hadoop/HDFS. It is modeled on [ElephantDB](https://github.com/nathanmarz/elephantdb "ElephantDB").

It uses Thrift to provide a network interface and [Krati](http://sna-projects.com/krati/ "Krati") for local persistence.

## Maven ##

    <dependencies>
        <dependency>
            <groupId>com.rubicon.oss</groupId>
            <artifactId>valkyriedb</artifactId>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>samtingleff-maven-snapshot-repo</id>
            <url>https://github.com/samtingleff/maven-repo/raw/master/snapshots</url>
            <layout>default</layout>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>daily</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
