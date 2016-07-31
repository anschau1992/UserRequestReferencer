package subclassification.stanfordNLP;

/**
 * All combination of the four typedDependencies AUX, DOBJ, NSUBJ and XCOMP of the Stanford NLP
 */
public enum  NLPType {
    NOTHING,
    AUX,
    DOBJ,
    NSUBJ,
    XCOMP,
    AUX_DOBJ,
    AUX_NSUBJ,
    AUX_XCOMP,
    DOBJ_NSUBJ,
    DOBJ_XCOMP,
    NSUBJ_XCOMP,
    AUX_DOBJ_NSUBJ,
    AUX_DOBJ_XCOMP,
    AUX_NSUBJ_XCOMP,
    DOBJ_NSUBJ_XCOMP,
    AUX_DOBJ_NSUBJ_XCOMP
}
