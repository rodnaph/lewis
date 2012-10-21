
Lewis
=====

Lewis is meant to be a web interface for interacting with Datomic.
I'm writing it to learn Datomic, so beware!  The information given
below is Readme Driven Development, so is probably incomplete.

Installation
------------

Clone the repo and then run Lewis with Leiningen.

```
git clone https://github.com/rodnaph/lewis.git
cd lewis
lein run
```

You can then open your browser to _http://localhost:5555_, and you
should see the web interface where you can connect.  Just enter the
Datomic URI to your database.

Usage
=====

When you have connected to Datomic, you will see some options in the
top navigation bar.

Schema
------

This will display information about the schema of your Datomic database,
and allow you to edit/update it.

Query
-----

This allows you to perform arbitrary Datalog queries on your Datomic database.

Transact
--------

This allows you to execute arbitrary transations.

