FROM couchbase:community

RUN chmod +x /entrypoint.sh
COPY init-couchbase.sh /opt/couchbase
RUN chmod +x /opt/couchbase/init-couchbase.sh

CMD ["/opt/couchbase/init-couchbase.sh"]