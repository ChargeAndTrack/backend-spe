var conn = new Mongo();
var db = conn.getDB('ChargeAndTrackDB');

db.createCollection('users');
db.createCollection('chargingStations');

try {
   db.users.deleteMany( { } );
} catch (e) {
   print (e);
}

var cursor = db.users.find();
while ( cursor.hasNext() ) {
   printjson( cursor.next() );
}

db.users.insertMany([
   {"username": "admin", "password": "admin1234", "role": "ADMIN"},
   {"username": "user1", "password": "user11234", "role": "BASE_USER"},
   {"username": "user2", "password": "user21234", "role": "BASE_USER"}
]);
db.chargingStations.createIndex({ location: "2dsphere" });

var cursor = db.users.find();
while ( cursor.hasNext() ) {
   printjson( cursor.next() );
}
