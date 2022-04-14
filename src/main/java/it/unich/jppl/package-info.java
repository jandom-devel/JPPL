/**
 * This package contains all the classes of the JPPL.
 *
 * <p>
 * The central cornerstones of the JPPL are the two interfaces {@link Domain}
 * and {@link Property}. Every nunerical abstract domain should implement both.
 * {@link Property} is the interface for the elements of an abstract domain,
 * which we will call <em>abstract objects</em>. Methods of the {@link Property}
 * interface are operations which may be performed on these abstract objects,
 * such as projection over a dimension, convex hull, refinement with a linear
 * constraint, etc... F-Bounded polymorphism is used for correctly typing the
 * parameters and return value of these methods. A {@link Domain} is a factory
 * for abstract objects. Each class implementing {@link Property} has a
 * companion class implementing {@link Domain} (for example, {@link CPolyhedron}
 * implements {@link Property} while {@link CPolyhedronDomain} implements
 * {@link Domain}).
 * </p>
 *
 * <p>
 * Almost all methods of all classes throw {@link PPLRuntimeException} when the
 * underlying PPL library generates an error. Note that the PPL is not thread
 * safe, therefore the same holds for the JPPL.
 * </p>
 */

package it.unich.jppl;
