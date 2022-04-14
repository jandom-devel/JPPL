package it.unich.jppl;

import java.util.Iterator;

/**
 * A system of (syntactic) geometric descriptors.
 *
 * <p>
 * This the common interface for systems of geometric descriptors of type
 * {@code GD}, i.e., multisets of objects of the class {@code GD}. When
 * inserting geometric descriptors in a system, space dimensions are
 * automatically adjusted so that all the descriptors in the system are defined
 * over the same vector space.
 * </p>
 */
public interface GeometricDescriptorsSystem<GD, GDS extends GeometricDescriptorsSystem<GD, GDS>>
        extends PPLObject<GDS>, Iterable<GD> {

    /**
     * Returns the space dimension of this system.
     */
    long getSpaceDimension();

    /**
     * Returns true if and only if this system contains no (non-trivial) geometric
     * descriptors.
     */
    boolean isEmpty();

    /**
     * Removes all geometric descriptors from this system.
     *
     * @return this system
     */
    GDS clear();

    /**
     * Adds the geometric decriptor d to this system.
     *
     * @return this system
     */
    GDS add(GD gd);

    /**
     * Returns an iterator over the elements of this system.
     */
    Iterator<GD> iterator();

}
