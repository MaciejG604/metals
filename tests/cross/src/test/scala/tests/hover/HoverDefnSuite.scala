package tests.hover

import tests.pc.BaseHoverSuite

class HoverDefnSuite extends BaseHoverSuite {

  check(
    "val",
    """object a {
      |  <<val @@x = List(1)>>
      |}
      |""".stripMargin,
    """|val x: List[Int]
       |""".stripMargin.hover
  )

  check(
    "var",
    """object a {
      |  <<var @@x = List(1)>>
      |}
      |""".stripMargin,
    """|var x: List[Int]
       |""".stripMargin.hover
  )

  check(
    "def-nullary",
    """object a {
      |  <<def @@x = List(1)>>
      |}
      |""".stripMargin,
    """|def x: List[Int]
       |""".stripMargin.hover
  )

  check(
    "def-params",
    """object a {
      |  <<def @@method(x: Int) = List(x)>>
      |}
      |""".stripMargin,
    """|def method(x: Int): List[Int]
       |""".stripMargin.hover
  )

  check(
    "def-tparams",
    """object a {
      |  <<def @@empty[T] = Option.empty[T]>>
      |}
      |""".stripMargin,
    """|def empty[T]: Option[T]
       |""".stripMargin.hover
  )

  check(
    "context-bound",
    """object a {
      |  <<def @@empty[T:Ordering] = Option.empty[T]>>
      |}
      |""".stripMargin,
    "def empty[T: Ordering]: Option[T]".hover
  )

  check(
    "lambda-param",
    """object a {
      |  List(1).map(<<@@x>> => )
      |}
      |""".stripMargin,
    """|```scala
       |x: Int
       |```
       |""".stripMargin
  )

  check(
    "param",
    """object a {
      |  def method(<<@@x: Int>>): Int = x
      |}
      |""".stripMargin,
    """|```scala
       |x: Int
       |```
       |""".stripMargin
  )

  check(
    "ctor",
    """class a {
      |  <<def t@@his(x: Int) = this()>>
      |}
      |""".stripMargin,
    """|```scala
       |def this(x: Int): a
       |```
       |""".stripMargin
  )

  check(
    "ctor-param",
    """class a {
      |  def this(<<@@x: Int>>) = this()
      |}
      |""".stripMargin,
    """|```scala
       |x: Int
       |```
       |""".stripMargin
  )

  check(
    "implicit-param",
    """class a {
      |  def method(implicit <<@@x: Int>>) = this()
      |}
      |""".stripMargin,
    """|```scala
       |implicit x: Int
       |```
       |""".stripMargin
  )

  check(
    "implicit-param2",
    """class a {
      |  def method(implicit y: Int, <<@@x: Int>>) = this()
      |}
      |""".stripMargin,
    """|```scala
       |implicit x: Int
       |```
       |""".stripMargin
  )

  check(
    "object",
    """object <<M@@yObject>>
      |""".stripMargin,
    "object object.MyObject".hover,
    compat = Map(
      "3" -> "object MyObject: `object`".hover
    )
  )

  check(
    "trait",
    """trait <<M@@yTrait>>
      |""".stripMargin,
    "abstract trait MyTrait: MyTrait".hover,
    compat = Map(
      "3" -> "trait MyTrait: MyTrait".hover
    )
  )

  check(
    "class",
    """class <<M@@yClass>>
      |""".stripMargin,
    "class MyClass: MyClass".hover
  )

  check(
    "type",
    """|object a {
       |  type <<M@@yType>> = Int
       |}""".stripMargin,
    "type MyType: MyType".hover
  )

  check(
    "package",
    """package b.p@@kg
      |object Main
      |""".stripMargin,
    """```scala
      |package b.pkg
      |```
      |""".stripMargin,
    automaticPackage = false,
    compat = Map(
      // TODO hover doesn't show information on package
      "3" -> "".hover
    )
  )

  check(
    "pat-bind",
    """
      |object Main {
      |  List(1) match {
      |    case h@@ead :: _ =>
      |  }
      |}
      |""".stripMargin,
    "head: Int".hover,
    compat = Map(
      "3" -> "val head: Int".hover
    )
  )

  check(
    "pat-bind2",
    """
      |object Main {
      |  Option(1) match {
      |    case Some(val@@ue) =>
      |  }
      |}
      |""".stripMargin,
    "value: Int".hover,
    compat = Map(
      "3" -> "val value: Int".hover
    )
  )

  check(
    "val-int-literal".tag(IgnoreScalaVersion.forLessThan("2.13.0")),
    """object a {
      |  <<val @@x : 1 = 1>>
      |}
      |""".stripMargin,
    """|val x: 1
       |""".stripMargin.hover,
    compat = Map(
      "3" ->
        """|Int
           |val x: 1""".stripMargin.hover
    )
  )

  check(
    "val-int-literal-union".tag(IgnoreScala2),
    """object a {
      |  <<val @@x : 1 | 2 = 1>>
      |}
      |""".stripMargin,
    "val x: 1 | 2".hover
  )

  check(
    "dealias-appliedtype-params",
    """|trait Base {
       |  type T
       |  def f(t: T): Option[T] = Some(t)
       |}
       |
       |object Derived extends Base {
       |  override type T = Int
       |}
       |object O {
       |  <<val @@x = Derived.f(42)>>
       |}
       |""".stripMargin,
    """|```scala
       |val x: Option[Int]
       |```
       |""".stripMargin,
    compat = Map(
      "3" ->
        """|**Expression type**:
           |```scala
           |Option[Int]
           |```
           |**Symbol signature**:
           |```scala
           |val x: Option[T]
           |```
           |""".stripMargin.hover
    )
  )

  check(
    "trailing-whitespace",
    """|object A {
       |
       |  <<val foo@@ = 123>>
       |}
       |""".stripMargin,
    "val foo: Int".hover
  )
}
