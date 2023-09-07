FROM eclipse-temurin:17
ENV APP_ROOT /usr/src/app
WORKDIR $APP_ROOT
USER root
RUN apt-get update -y \
    && apt-get install -y curl zip bash \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
RUN ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2
RUN curl -s https://get.sdkman.io | bash
RUN bash -c " \
	source "$HOME/.sdkman/bin/sdkman-init.sh" && \
	sdk install kotlin 1.3.72 \
	"