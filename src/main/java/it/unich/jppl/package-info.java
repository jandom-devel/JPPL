/**
 * <p>
 * This is the package contining all the classes of JPPL.
 * </p>
 *
 * <p>
 * The central corenerstones of JPPL are the two interfaces
 * {@link it.unich.jppl.Domain Domain} and {@link it.unich.jppl.Property
 * Property}. Every class which implements a numerical domain should implement
 * the Property interface. Methods of the Property interface are operations
 * which may be performed on geometric objects, such as projection over a
 * dimension, convex hull, refinement with a linear expressions, etc...
 * F-Bounded polymorphism is used for correctly typing the parameters and return
 * value of these methods.
 * </p>
 *
 * <p>
 * A Domain is a factory for geometric objects. Each class implementing Property
 * has a companion class implementing Domain (for example, Polyhedron implements
 * Property while PolyhedronDomain implements Domain). Domain has a single
 * method called {@code createProperty} in several overloaded variants which
 * returns a property of the corresponding class.
 * </p>
 *
 * <p>
 * Almost all methods tof all classes throw {@link PPLError} when the underlying
 * PPL library generates an error.
 * </p>
 */

package it.unich.jppl;
