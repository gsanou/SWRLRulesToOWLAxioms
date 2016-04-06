package rulesToAxioms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

public class Srd {

	public static OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

	// SWRL Related Methods

	protected static ArrayList<SWRLArgument> getArgumentsToArrayList(SWRLAtom atom) {
		ArrayList<SWRLArgument> arguments = new ArrayList<SWRLArgument>();
		Iterator<SWRLArgument> iterator = atom.getAllArguments().iterator();
		while (iterator.hasNext())
			arguments.add(iterator.next());
		return arguments;
	}

	protected static ArrayList<SWRLVariable> getVariablesToArrayList(SWRLAtom atom) {
		ArrayList<SWRLVariable> variables = new ArrayList<SWRLVariable>();
		for (SWRLArgument argument : atom.getAllArguments())
			if (isVariable(argument))
				variables.add((SWRLVariable) argument);
		return variables;
	}

	public static Set<SWRLArgument> getArgumentsToSet(Set<SWRLAtom> atoms) {
		Set<SWRLArgument> arguments = new HashSet<SWRLArgument>();
		for (SWRLAtom atom : atoms)
			arguments.addAll(atom.getAllArguments());
		return arguments;
	}

	protected static Set<SWRLVariable> getVariablesToSet(Set<SWRLAtom> atoms) {
		Set<SWRLVariable> variables = new HashSet<SWRLVariable>();
		for (SWRLAtom atom : atoms)
			variables.addAll(getVariablesToSet(atom));
		return variables;
	}

	protected static Set<SWRLVariable> getVariablesToSet(SWRLAtom atom) {
		Set<SWRLVariable> variables = new HashSet<SWRLVariable>();
		for (SWRLArgument argument : atom.getAllArguments())
			if (Srd.isVariable(argument))
				variables.add((SWRLVariable) argument);
		return variables;
	}

	protected static boolean isVariable(SWRLArgument argument) {
		return argument.toString().contains("Variable");
	}

	// Inverse Role Related Methods

	protected static boolean isInverse(OWLObjectPropertyExpression role) {
		return (isInverse(role.toString()));
	}

	protected static boolean isInverse(OWLDataPropertyExpression role) {
		return (isInverse(role.toString()));
	}

	protected static boolean isInverse(String role) {
		return (role.contains("InverseOf"));
	}

	protected static OWLObjectPropertyExpression invert(OWLObjectPropertyExpression role) {
		if (isInverse(role))
			return role.getNamedProperty();
		else
			return role.getInverseProperty();
	}

	protected static String invert(String role) {
		if (role.contains("InverseOf"))
			return role.substring(10, role.length() - 1);
		else
			return "InverseOf(" + role + ")";
	}

	// Visualization Methods

	public static String toString(Set<SWRLAtom> body) {
		String bodyString = new String();
		for (SWRLAtom bodyAtom : body)
			bodyString += toString(bodyAtom) + ", ";
		return bodyString.substring(0, bodyString.length() - 2);
	}

	protected static String toString(SWRLAtom atom) {
		String predicateStr = new String(atom.getPredicate().toString());
		Collection<SWRLArgument> arguments = atom.getAllArguments();
		Iterator<SWRLArgument> iterator = arguments.iterator();
		SWRLArgument arg0 = iterator.next();

		if (arguments.size() == 1)
			return predicateStr + "(" + trim(arg0.toString()) + ")";
		else {
			SWRLArgument arg1 = iterator.next();
			return predicateStr + "(" + trim(arg0.toString()) + ", " + trim(arg1.toString()) + ")";
		}
	}

	private static String trim(String argStr) {
		return argStr.substring(argStr.indexOf("<"), argStr.indexOf(">") + 1);
	}

	public static String toString(SWRLRule testRule) {
		return toString(testRule.getBody()) + " -> " + toString(testRule.getHead()) + ".";
	}

}