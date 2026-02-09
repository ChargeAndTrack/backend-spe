FROM filopracu/mongonovolume

ADD mydbinit.js /docker-entrypoint-initdb.d/
RUN set -eux; \
	chown mongodb /docker-entrypoint-initdb.d/mydbinit.js; \
	chgrp mongodb /docker-entrypoint-initdb.d/mydbinit.js; \
	chmod 755 /docker-entrypoint-initdb.d/mydbinit.js

ENTRYPOINT ["docker-entrypoint.sh"]
EXPOSE 27017 27018 27019
CMD ["mongod"]
