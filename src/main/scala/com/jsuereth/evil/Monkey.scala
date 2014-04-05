package com.jsuereth.evil

import scala.language.dynamics
import scala.reflect.ClassTag
import java.lang.reflect.{Field,Method}

/** A class to violate all rules regarding the underlying target.
 *
 *
 *  This is designed to make working with legacy APIs which don't expose
 *  necessary internals to fix bugs.  Sometimes you need an evil monkey.
 */
class Monkey(val target: AnyRef) extends EvilMonkeyLike {
  def targetClass = target.getClass

}
class StaticMonkey(val targetClass: Class[_]) extends EvilMonkeyLike {
  def target = null
}
object Monkey {
  def apply(target: AnyRef): Monkey = new Monkey(target)
  def static[T](implicit c: ClassTag[T]): StaticMonkey =
    new StaticMonkey(c.erasure)
}


// Helper to handle static vs. live monkeys.
trait EvilMonkeyLike extends Dynamic {
  def targetClass: Class[_]
  def target: AnyRef


  // Cheat fields
  def selectDynamic[T](name: String)(implicit t: ClassTag[T]): Option[T] = 
    withField(name)(_.get(target).asInstanceOf[T])
  def updateDynamic[T](name: String)(value: Any): Unit =
    withField(name)(_.set(target, value))

  // Cheat methods
  def applyDynamic[T](name: String)(args: Any*): Option[T] = 
    withMethod(name)(args.map(_.getClass):_*) { method =>
      method.invoke(target, args.map(_.asInstanceOf[AnyRef]):_*).asInstanceOf[T]
    }
  private def withMethod[A](name: String)(args: Class[_]*)(f: Method => A): Option[A] = 
    reflectiveLookup { cls =>
      cls.getDeclaredMethod(name, args:_*)
    } map { method =>
      f(method)
    }
  // TODO - memoizers
  private def withField[A](name: String)(f: Field => A): Option[A] =
    reflectiveLookup(_.getDeclaredField(name)) map f
  // TODO - not needed in JDK7
  private type AccessibleObject = {
    def setAccessible(value: Boolean): Unit
  }
  private def reflectiveLookup[A <: AccessibleObject](f: Class[_] => A): Option[A] =
    try {
      val thing = f(targetClass)
      import scala.language.reflectiveCalls
      thing.setAccessible(true)
      Some(thing)
    } catch {
      case (_: java.lang.NoSuchFieldException) |
           (_: java.lang.SecurityException) |
           (_: java.lang.NoSuchMethodException) => None
    }
}


