# Testing ORIS

## Manual Tests
Manual way to test ORIS is to load data into ORIS and run the *automatic prediction module*. 
The *automatic prediction module* executes all the methods implemented in ORIS for finding origin of replication sequences.
The results can further be validated for model genomes by looking up the annotations or published experimental results.

Data for few well annotated genomes is provided in this directory.


## Experimental validation

The functionality of ORIS was tested be performing a case study (https://github.com/urmi-21/ORIS/blob/master/ORIS_SI.pdf), where we comprehensively tested ORIS using bacterial and archeal genomes. We also demonstrated application of ORIS on unannotated genomes. The results are compared to previously experimentally validated and published results and with results from other published tools. The results demonstrate that ORIS is able to find the correct origin of replication sequences in the given genomes.
Another, example for validation of ORIS’ result is the work published by Agarwal et. al. (https://www.ncbi.nlm.nih.gov/pubmed/28644560). In this work ORIS predicted putative origin of replication sites in the genome of _Plasmodium falciparum_. The computational results were later experimentally verified.
