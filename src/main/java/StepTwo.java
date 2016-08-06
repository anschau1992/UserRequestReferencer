import codeLinking.SourceCodeLinker;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import crawler.Constants;
import helper.Review;
import helper.TrainingSetToDB;
import preclassification.PreClassification;
import preclassification.PreclassificationDBImport;
import subclassification.SubClassifier;

import java.util.List;
import java.util.Set;

//TODO: better name
public class StepTwo implements Constants {
    public static void main(String[] args) throws Exception {
        argumentCheck(args);
        String dbName;

        PreClassification preclassification = PreClassification.valueOf(args[0]);
        if(args[1].equals("test")) {
            dbName = DBNAME_TEST;
        } else {
            dbName = DBNAME;
        }

        //2. Update review's preclassification
        PreclassificationDBImport preClassImport = new PreclassificationDBImport(dbName);
        preClassImport.importPreClassificationIntoDB();

        //write trainingSet into DB if not done yet
        if(!preClassImport.collectionExists(TRAININGSET_COLLECTION)) {
            System.out.println("Loading Training-Set into DB");
            TrainingSetToDB trainingSetToDB = new TrainingSetToDB(dbName);
            trainingSetToDB.writeTrainingSetIntoDB();
        }

        //3. Subclassify the reviews
        SubClassifier subClassifier = new SubClassifier(dbName, preclassification);
        List<Review> reviews = subClassifier.subClassify();

        //TODO run SoureCodeLinker
        //4. SourceCode Linking
        SourceCodeLinker sourceCodeLinker = new SourceCodeLinker(reviews, dbName);

    }

    /**
     * Checks the given argument. Prints out crresponding error-message
     * @param args
     */
    private static void argumentCheck(String[] args) {
        PreClassification preclassification = null;
        if(args.length != 2) {
            System.out.println("Wrong number of arguments! Use of program: java StepTwo <preclassification> <test|prod>");
            System.out.println("Whereas <preclassification> is one of the following arguments:");
            System.out.println("\t RESSOURCES, PRICING, PROTECTION, USAGE, COMPATIBILITY");

            System.exit(0);
        }

        try {
            preclassification = PreClassification.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong preclassification! Use one of the following as first argument:");
            System.out.println("\t RESSOURCES, PRICING, PROTECTION, USAGE, COMPATIBILITY");
            System.exit(0);
        }

        if(!args[1].equals("test")  && !args[1].equals("prod")) {
            System.out.println("Wrong preclassification! Use one of the following as second argument:");
            System.out.println("test, prod");
            System.exit(0);
        }
    }



}
