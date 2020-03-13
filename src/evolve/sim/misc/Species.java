package evolve.sim.misc;

import java.awt.Color;

import evolve.sim.neuralNet.Mutateable;

/**
 * An object that keeps track of the species of an object
 */
public class Species{

	/**
	 * The minimum value of a species object
	 */
	public static final double MIN_VALUE = 0;
	/**
	 * The maximum value of a species object
	 */
	public static final double MAX_VALUE = 1;
	
	/**
	 * The gene that controls the value of this species object
	 */
	private Gene species;
	
	/**
	 * Creates a species with the specified value
	 * @param value the initial value of this species
	 */
	public Species(double value){
		setSpeciesGene(new Gene(value, 0, 1, 1));
	}
	
	/**
	 * Create a species object with a randomized value
	 */
	public Species(){
		this(0);
		this.species.setValue(Mutateable.randomValue(0, 1, 1, 0));
	}
	
	/**
	 * Get the gene controlling this species
	 * @return the gene
	 */
	public Gene getSpeciesGene(){
		return species;
	}
	/**
	 * Set the species gene of this Species object to the given gene<be>
	 * The gene is automatically set to modulo if it is not already
	 * @param species the gene to set
	 */
	public void setSpeciesGene(Gene species){
		this.species = species;
		this.species.setModulo(true);
	}
	/**
	 * Get the value of this Species object
	 * @return the value
	 */
	public double getSpeciesValue(){
		return species.getValue();
	}
	/**
	 * Set the value of the species gene of this species to the given species
	 * @param species the value of the gene to set
	 */
	public void setSpeciesValue(double species){
		this.species.setValue(species);
	}
	
	/**
	 * Approach the given species, if the distance is small enough, this species will be the same value as the given species. 
	 * If the species are already the same, then no change happens. 
	 * If the change goes over the approaching species, then the two species will be the same in the end
	 * @param species the species to approach
	 * @param modifier the modifier to change the amount the species should change by
	 */
	public void approachSpecies(Species species, double modifier){
		//compare the species
		double compare = compareSpecies(species);

		//find the difference between the species
		double diff = Math.abs(getSpeciesValue() - species.getSpeciesValue());
		
		//find the smaller distance in the modular sense
		double change =  Math.min(compare, diff);

		//modify the species based on how far is is from the given species, a random amount, and the modifier
		double speciesChange = change * Math.random() * modifier;
		if(speciesChange > change) speciesChange = change;
		
		//determine if the speciesChange should be added or subtracted
		boolean thisLow = getSpeciesValue() < species.getSpeciesValue();
		boolean diffCase = diff == change;
		if(diffCase != thisLow) speciesChange *= -1;
		
		setSpeciesValue(getSpeciesValue() + speciesChange);
	}
	
	/**
	 * Get the value used by {@link Color.HSBtoRGB(hue, saturation, brightness)} that represents the hue of this species
	 * @return
	 */
	public float getHue(){
		return (float)(getSpeciesValue());
	}
	
	/**
	 * Compare the species value of this species and the given species, and return the minimum distance in a modular sense of the two species.<br><br>
	 * For example, if one species is 0.5, and the other is 0.6, the value returned is 0.1<br>
	 * if one species is 0.1, and the other is 0.9, then the value returned is 0.2<br>
	 * if one species is 0.0, and the other is 1.0, then the value returned is 0.0<br>
	 * if one species is 0.5, and the other is 1.0, the value returned is 0.5<br>
	 * @param s the species to compare
	 * @return the minimum distance in species, always in range [0.0, 0.5]
	 */
	public double compareSpecies(Species s){
		return Mutateable.compareModular(getSpeciesValue(), s.getSpeciesValue(), 1);
	}
	
	/**
	 * Get the color that represents this species with the given saturation and brightness
	 * @param sat the saturation
	 * @param bright the brightness
	 * @return the color of this species
	 */
	public Color getColor(float sat, float bright){
		return new Color(Color.HSBtoRGB(getHue(), sat, bright));
	}
	
	public Species copy(){
		Species s = new Species(this.species.getValue());
		s.setSpeciesGene(getSpeciesGene().copy());
		return s;
	}
	
}
