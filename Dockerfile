FROM openjdk:11.0-jre-slim

MAINTAINER Elijah Zupancic <elijah@zupancic.name>

# Metadata for Docker containers: http://label-schema.org/
LABEL org.label-schema.name="product-record-ingestor-library" \
      org.label-schema.description="Product Information Ingestion Library Exercise" \
      org.label-schema.url="https://github.com/dekobon/product-record-ingestor-exercise" \
      org.label-schema.vcs-url="org.label-schema.vcs-ref" \
      org.label-schema.schema-version="1.0"

ENV VERSION 1.0-SNAPSHOT

RUN groupadd -g 1244 product-record-ingestor && \
    useradd -g 1244 -u 1244 -c 'Product Record Ingestor' -d /opt/product-record-ingestor -r -s /bin/false product-record-ingestor && \
    mkdir -p /opt/product-record-ingestor && \
    chown -R product-record-ingestor:product-record-ingestor /opt/product-record-ingestor

ADD --chown=product-record-ingestor:product-record-ingestor "https://github.com/dekobon/product-record-ingestor-exercise/releases/download/$VERSION/swiftly-exercise-$VERSION-jar-with-dependencies.jar" /opt/product-record-ingestor/swiftly-exercise.jar

RUN echo '452b603dd40d422dcd8051e517939877471cd8734c1a24d15d5a16558ed2378d  /opt/product-record-ingestor/swiftly-exercise.jar' | sha256sum -c

WORKDIR /opt/product-record-ingestor
USER product-record-ingestor

# Using entrypoint allows the user to pass a parameter when executing the docker
# image so that they can specify the record data file
ENTRYPOINT ["java", "-jar", "/opt/product-record-ingestor/swiftly-exercise.jar"]
