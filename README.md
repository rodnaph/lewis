Lewis
=====

Lewis is meant to be a web interface for interacting with Datomic.
I'm writing it to learn Datomic, so beware!

Installation
------------

Clone the repo and then run Lewis with Leiningen.

```
git clone https://github.com/rodnaph/lewis.git
cd lewis
lein ring server
```

Your  browser will automatically open to _http://localhost:5555_, and you
should see the web interface where you can connect.  Just enter the
Datomic URI to your database.

Usage
=====

When you have connected to Datomic, you will see some options in the top navigation bar.

Data
----

The data menu gives you _Query_ option, which will present an editor that you can
enter arbitrary Datalog queries in.

![](http://github.com/rodnaph/lewis/raw/master/screenshots/query.png)

And there is also an _Insert_ option, which gives a simple interface for entering
data into your Datomic database.

![](http://github.com/rodnaph/lewis/raw/master/screenshots/insert.png)

Schema
--------

The schema menu has options for browsing and editing the schema of your database.

![](http://github.com/rodnaph/lewis/raw/master/screenshots/schema.png)

And also just executing arbitrary transactions.

![](http://github.com/rodnaph/lewis/raw/master/screenshots/transact.png)

There is also a page to edit schema.  It has a bunch of controls so you can select
the schema attributes you want, and the EDN is created for you below.

![](http://github.com/rodnaph/lewis/raw/master/screenshots/update.png)

Production
==========

To use in production, run `lein run` in the main directory to launch a jetty server.

TODO
====

I'm hacking on this project to learn more about Datomic, so not even sure if what I've
done so far is useful (or makes sense).  Making up features as I go then...  but if
you do have any ideas please just open an issue or get in touch.
