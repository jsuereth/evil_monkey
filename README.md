# Evil Monkey #
Because sometimes you're working with legacy code and there is no better way.

Might as well enjoy it while doing evil.


## Examples ##

Note: All these methods are private, final, static or all three.

```scala
$ sbt test:console
scala> val cls = com.jsuereth.evil.Monkey.static[Evil]
cls: com.jsuereth.evil.StaticMonkey = com.jsuereth.evil.StaticMonkey@517e109e

scala> cls.bar[Int]
res9: Option[Int] = Some(2)

scala> val monkey = com.jsuereth.evil.Monkey(new Evil())
monkey: com.jsuereth.evil.Monkey = com.jsuereth.evil.Monkey@2587053f 

scala> monkey.foo[Int]
res10: Option[Int] = Some(1)

scala> monkey.foo = 3
monkey.foo: Option[Nothing] = Some(3)

scala> monkey.dumb("Hello")
DUMB!!! Hello
res11: Option[Nothing] = Some(null)

scala> monkey.dumb2[String]("Hello, Again")
res12: Option[String] = Some(Hello, Againmore)
```

