package example.nested

trait LocalDeclarations {
  def foo(): Unit
}

trait Foo {
  val y/*: Int*/ = 3
}

object LocalDeclarations {
  def create(): LocalDeclarations = {
    def bar(): Unit = ()

    val x/*: AnyRef{val x: Int}*/ = new {
      val x/*: Int*/ = 2
    }

    val y/*: Foo*/ = new Foo {}

    x.x + y.y

    new LocalDeclarations with Foo {
      override def foo(): Unit = bar()
    }

  }
}