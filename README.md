# JPPL

The JPPL is a set of Java bindings for the PPL (Parma Plyhedra Library) written using the JNA (Java Native Acccess) library.

Although the PPL already comes with Java bindings (in the following called standard bindings), we think they have some shortcomings:

  * They are not available in standard online repositories and scarcely available in OS package repositories (for example, in the Linux world, the Java bindings are packaged by Fedora but not by Debian or Ubuntu).
  * The abstract domains do not explicitly implement a common interface. This make generic programming impossible without recurring to reflexivity.
  * Most conventions do not follow the Java standard practices (for example, words in multi-word class names are separated by an underscore).
  * Some parts of the API (such as the one for linear expressions) are cumbersome to use. 

JPPL strives to overcome these shortcomings. The fact that the standard bindings are not readily available online in binary form led us to write a complete replacement for them, instead of a wrapper. JPPL uses JNA to interface directly with the C bindings of the PPL. Obviously, this choice also has its shortcomings, mainly:

  * JPPL is probably slower than the standard bindings.
  * JPPL only works (at least at the moment) with the default configuration of the C bindings of the PPL.

Note that the PPL is not thread safe, therefore the same holds for the JPPL.
