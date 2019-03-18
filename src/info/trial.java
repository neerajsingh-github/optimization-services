
package info;
import java.lang.*;

import java.io.File;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


public class trial {

	public static void main (String[] args) {
		// TODO Auto-generated constructor stub
		System.out.println(" Welcome to Workspace : JOPS ; Project GAMS;");
				
		System.out.println(" Example: Toy Model for Bond Optimization: From Ex3 ");
        // check workspace info from command line arguments
        GAMSWorkspaceInfo  wsInfo  = new GAMSWorkspaceInfo();
        if (args.length > 0)
            wsInfo.setSystemDirectory( args[0] );
        // create a directory
        File workingDirectory = new File(System.getProperty("user.dir"), "Transport3");
        workingDirectory.mkdir();
        wsInfo.setWorkingDirectory(workingDirectory.getAbsolutePath());
        // create a workspace
        GAMSWorkspace ws = new GAMSWorkspace(wsInfo);

        // Create and run a job from a data file, then explicitly export to a GDX file
        GAMSJob t3 = ws.addJobFromString(data);
        t3.run();
        t3.OutDB().export( ws.workingDirectory() + GAMSGlobals.FILE_SEPARATOR + "tdata.gdx" );

        // run a job using an instance of GAMSOptions that defines the data include file
        t3 = ws.addJobFromString(model);

        GAMSOptions opt = ws.addOptions();
        opt.defines("gdxincname", "tdata");
        t3.run(opt);

        GAMSVariable x = t3.OutDB().getVariable("x");
        for (GAMSVariableRecord rec : x)
            System.out.println("x(" + rec.getKey(0) + ", " + rec.getKey(1) + "): level=" + rec.getLevel() + " marginal=" + rec.getMarginal());
        System.out.println();

        // similar to the previous run but without exporting database into a file
        GAMSJob t3a = ws.addJobFromString(data);
        GAMSJob t3b = ws.addJobFromString(model);
        opt = ws.addOptions();

        t3a.run();

        opt.defines("gdxincname", t3a.OutDB().getName());
        t3b.run(opt, t3a.OutDB());

        for(GAMSVariableRecord rec : t3b.OutDB().getVariable("x"))
            System.out.println("x(" + rec.getKey(0) + ", " + rec.getKey(1) + "): level=" + rec.getLevel() + " marginal=" + rec.getMarginal());
        
		
	}
	
	
	    static String data =
	               "Sets                                                             \n" +
	               "  i   variableindex          / 1, 2 / ;                          \n" +
	               "Parameters                                                       \n" +
	               "                                                                 \n" +
	               "  k(i)  capacity of plant i in cases                             \n" +
	               "                     /    1     3                               \n" +
	               "                          2   4  / ;                             \n" ;


	     static String model = "Sets                                                           \n" +
	               "      i  variableindex                                                   \n" +
	              
	               "                                                                           \n" +
	               " Parameters                                                                \n" +
	               "      k(i)   capacity of plant i in cases                                  \n" +
	              
	               "                                                                           \n" +
	               "$if not set gdxincname $abort 'no include file name for data file provided'\n" +
	               "$gdxin %gdxincname%                                                        \n" +
	               "$load i j a b d f                                                          \n" +
	               "$gdxin                                                                     \n" +
	               "                                                                           \n" +
	           
	               "                                                                           \n" +
	               " Variables                                                                 \n" +
	               "       x(i)  shipment quantities in cases                                \n" +
	             
	               "                                                                           \n" +
	               " Positive Variable x ;                                                     \n" +
	               "                                                                           \n" +
	               " Equations                                                                 \n" +
	               "                                                                           \n" +
	               "      cost        define objective function                                \n" +
	               
	               
	               "                                                                           \n" +
	               "  cost ..        K  =e=  sum((i), k(i)*x(i)) ;                       		\n" +
	               "                                                                           \n" +
	              
	              
	               "                                                                           \n" +
	               " Model transport /all/ ;                                                   \n" +
	               "                                                                           \n" +
	               " Solve transport using lp minimizing K ;                                   \n" +
	               "                                                                           \n" +
	               " Display x.l, x.m ;                                                        \n" +
	               "                                                                           \n";
	
	
}

	
	
	/*	
	static String data =
            "Sets                                                             \n" +
            "  i   vidx   / 1 * 6 /; 						                   \n" +
            "Parameters                                                       \n" +
            "                                                                 \n" +
            "  k1(i)  constsensi 	i in cases                             \n" +
            "                     /    1     1                        \n" +
            "                         2     1.5                        \n" +
            "                         3     -2                        \n" +
            "                         4     0                        \n" +
            "                         5     0                        \n" +
            "                         6     0  /                        \n" +
         
            "  k1Tilde(i)  eCoeff1 	i in cases                             \n" +
            "                     /    1     1                        \n" +
            "                         2     0                        \n" +
            "                         3     1.4                        \n" +
            "                         4     0                        \n" +
            "                         5     0                        \n" +
            "                         6     0  /                        \n" +
            "  p1(i)  PV1 	i in cases                             \n" +
            "                     /    1     1.5                        \n" +
            "                         2     1                        \n" +
            "                         3     1.1                        \n" +
            "                         4     0                        \n" +
            "                         5     0                        \n" +
            "                         6     0  /                        \n" +
            "  k2(i)  kostSensi2 	i in cases                             \n" +
            "                     /    1     1.4                        \n" +
            "                         2     -1.5                        \n" +
            "                         3     -0.5                        \n" +
            "                         4     2                        \n" +
            "                         5     2.8                        \n" +
            "                         6     3.8  /                        \n" +
            "  p2(i)  PV2 	i in cases                             \n" +
            "                     /    1     1.1                        \n" +
            "                         2     1.7                        \n" +
            "                         3     1.4                        \n" +
            "                         4     1.2                        \n" +
            "                         5     1.5                       \n" +
            "                         6     1.9  /                        \n" +
            "  k2Tilde(i)  eCoeff2 	i in cases                             \n" +
            "                     /    1     0                        \n" +
            "                         2     0                        \n" +
            "                         3     1.2                        \n" +
            "                         4     0                        \n" +
            "                         5     0                        \n" +
            "                         6     0  /                        \n" +
            "  k2Bar(i)  Bar2 	i in cases                             \n" +
            "                     /    1     1                        \n" +
            "                         2     1                        \n" +
            "                         3     1.4                        \n" +
            "                         4     0                        \n" +
            "                         5     0                        \n" +
            "                         6     0  /                     \n" +
      
	  "  Low(i)  Bound 	i in cases                             \n" +
      "                     /    1     0.5                        \n" +
      "                         2     1                        \n" +
      "                         3     6                        \n" +
      "                         4     2                        \n" +
      "                         5     3                        \n" +
      "                         6     0.1  /                       \n" + 
	"  Up(i)  Bound2 	i in cases                             \n" +
    "                     /    1     1.5                        \n" +
    "                         2     2                        \n" +
    "                         3     8                        \n" +
    "                         4     4                        \n" +
    "                         5     5                        \n" +
    "                         6     0.5  /       ;              \n" ;
	
	static String model = "                                                           \n" +
          " Variables 			                                                    \n" +
            "      x(i) in cases"
            + "    E1"
            + "    E2   ;                                                                \n" +
            " Equations                                                                 \n" +
            "                                                                           \n" +
            "      cost        define objective function                                \n" +
            //"      Risk       Risk                    							\n" +
           // "      F1         Finlueck1                             					\n" +
            //"      F2         Finlueck2 												\n" +
            // "    EB1        Eigen1 													\n" +	
             //"    EB2        Eigen2 													\n" +
             //"    L(i)       LowerBound at vidx i											\n" +
             "    U(i)       UpperBound	at vidx i;												\n" +
             "                                                                         \n" +
            "  cost ..        K  =e=  (sum((i), k1(i)*x(i))-E1*sum((i),k1tilde(i)*x(i))/(sum((i),p1(i)+x(i))) + (sum((i), k2(i)*x(i)) -E1*sum((i),k2tilde(i)*x(i) - E2*sum((i),k2bar(i)*x(i)))/(sum((i),p2(i)+x(i))  ));  \n" +
            "                                                                           \n" +
            "  Risk ..    sqrt(1.2*rPower(x(1),2) + 1.4*rPower(x(2),2) - 1.9*rPower(x(3),2) + rPower(x(4),2) + rPower(x(5),2) + 0.5*rPower(x(6),2) - 0.3* x(1)*x(2) ) =l= 5  ;  	\n" +
            "    \n" +
            "  L(i) .. x(i) =g= Low(i) ;                               \n" +
            "  U(i) .. x(i) =l= Up(i) ;                               \n" +
            "                                                                           \n" +
            " Model transport /all/ ;                                                   \n" +
            "                                                                           \n" +
            " Solve transport using lp minimizing K ;                                  \n" +
            "                                         	                                 \n" +
            " Display x.l, x.m, E1.l, E1.m ;                                               \n" +
            "                                                                           \n";

*/

