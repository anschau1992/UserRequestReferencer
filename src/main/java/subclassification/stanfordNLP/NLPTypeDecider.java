package subclassification.stanfordNLP;

import edu.stanford.nlp.trees.TypedDependency;

public class NLPTypeDecider {
    public NLPTypeDecider() {}

    public NLPType getNLPType(TypedDependency[] typedDependencies) {
        int listSize = typedDependencies.length;

        switch (listSize) {
            case 0:
                return NLPType.NOTHING;
            case 1:
                return getTypeForLengthOne(typedDependencies);
            case 2:
                return getTypeForLengthTwo(typedDependencies);
            case 3:
                return getTypeForLengthThree(typedDependencies);
            default:
                return NLPType.AUX_DOBJ_NSUBJ_XCOMP;
        }
    }

    private NLPType getTypeForLengthOne(TypedDependency[] typedDependencies) {
        String type = typedDependencies[0].toString();
        if (type.contains("aux")) {
            return NLPType.AUX;
        } else if (type.contains("dobj")) {
            return NLPType.DOBJ;
        } else if (type.contains("nsubj")) {
            return NLPType.NSUBJ;
        } else {
            return NLPType.XCOMP;
        }
    }

    private NLPType getTypeForLengthTwo(TypedDependency[] typedDependencies) {
        String types = typedDependencies[0].toString();
        types += typedDependencies[1].toString();

        if(types.contains("aux") && types.contains("dobj")) {
            return NLPType.AUX_DOBJ;
        }else if (types.contains("aux") && types.contains("nsubj")) {
            return NLPType.AUX_NSUBJ;
        }else if (types.contains("aux") && types.contains("xcomp")) {
            return NLPType.AUX_XCOMP;
        }else if (types.contains("dobj") && types.contains("nsubj")) {
            return NLPType.DOBJ_NSUBJ;
        }else if (types.contains("dobj") && types.contains("xcomp")) {
            return NLPType.DOBJ_XCOMP;
        } else {
            return NLPType.NSUBJ_XCOMP;
        }
    }

    private NLPType getTypeForLengthThree(TypedDependency[] typedDependencies) {
        String types = typedDependencies[0].toString();
        types += typedDependencies[1].toString();
        types += typedDependencies[2].toString();

        if(types.contains("aux") && types.contains("dobj") && types.contains("nsubj")) {
            return NLPType.AUX_DOBJ_NSUBJ;
        } else if(types.contains("aux") && types.contains("dobj") && types.contains("xcomp")) {
            return NLPType.AUX_DOBJ_XCOMP;
        } else if(types.contains("aux") && types.contains("nsubj") && types.contains("xcomp")) {
            return NLPType.AUX_NSUBJ_XCOMP;
        } else {
            return NLPType.DOBJ_NSUBJ_XCOMP;
        }
    }
}
