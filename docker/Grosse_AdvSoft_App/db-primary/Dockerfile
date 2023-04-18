FROM postgres:15-alpine
ENV GOSU_VERSION 1.16
RUN set -eux; \
    \
    apk add --no-cache --virtual .gosu-deps \
            ca-certificates \
            dpkg \
            gnupg \
    ; \
    \
    dpkgArch="$(dpkg --print-architecture | awk -F- '{ print $NF }')"; \
    wget -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch"; \
    wget -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch.asc"; \
    \
# clean up fetch dependencies
    apk del --no-network .gosu-deps; \
    \
    chmod +x /usr/local/bin/gosu; \
# verify that the binary works
    gosu --version; \
    gosu nobody true

COPY docker-entrypoint.sh /docker-entrypoint-initdb.d/docker-entrypoint.sh
RUN chmod 0666 /docker-entrypoint-initdb.d/docker-entrypoint.sh
