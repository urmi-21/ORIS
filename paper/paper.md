---
title: 'ORIS: An interactive software tool for prediction of replication origin in prokaryotic genomes'
tags:
  - Origin of replication
  - Whole genome analysis
  - GC-Skew
authors:
 - name: Urminder Singh
   orcid: 0000-0003-3703-0820
   affiliation: 1
 - name: Kushal Shah
   orcid: 0000-0003-3398-8425
   affiliation: 2
 - name: Suman Dhar
   affiliation: 3
 - name: Vinod Kumar Singh
   affiliation: 1
 - name: Annangarachari Krishnamachari
   affiliation: 1
affiliations:
 - name: School of Computational and Integrative Sciences, Jawaharlal Nehru University, New Delhi, India
   index: 1
 - name: Department of Electrical Engineering and Computer Science, Indian Institute of Science Education and Research (IISER), Bhopal - 462066, Madhya Pradesh, India
   index: 2
 - name: Special centre for Molecular Medicine, Jawaharlal Nehru University, New Delhi, India
   index: 3
date: 31 July 2019
bibliography: paper.bib
---

# Summary

A remarkable yet very precise event that takes place within the cell, is called DNA replication or chromosome copy [@alberts2013essential].
The way it is done in eukaryotes is vastly different from prokaryotes yet something is common as well. The origin of replication is a small segment in the genome at which the DNA replication process is initiated. Bacterial genomes generally have a single origin of DNA replication whereas archaea and eukaryotes have multiple replication sites [@o2013principles; @egan2005microreview].

Experimental studies carried out on the model organism *S. cerevisiae* brought greater insights in understanding the complex replication process. A typical origin sequence in *S. cerevisiae* has a DNA segment called Autonomous Replication Sequence (ARS) and this further has an essential A element and multiple B elements and all these are required for proper origin function or firing. This kind of organizational structure may be present in other genomes but is not very obvious. Uncovering such unknown structures in the genome can improve our understanding of DNA replication and the origin of replication sites.

Hence, software tools should be developed incorporating many such useful contextual information for sequence analysis, with the hope that biologists will benefit and it may provide them a helping hand to search for origin like sequences in any genome of their choice. 

There are a few online tools/databases which may help in finding the origin of replication sites for a few species [@frank2000oriloc; @gao2008ori]. These tools are limited by the type of computational approaches they take and are not interactive.

Here, we present a standalone software tool, ORIS, for interactive analysis of prokaryotic genomes to find the origin of replication sites in the genome.
An effort is made to develop a software suite that does context-based analysis which includes DSP (digital signal processing) based correlation measures, all types of skew measures and entropy-based methods for predicting the origin of replication [@beauchamp1979digital; @shah2012nucleotide].
Also, features such as pattern matching, generating sequence logo, finding entropy distribution along the length of the genome, DNA bending profiles, and composition statistics are available as part of the analysis. 

ORIS is a novel tool that lets the user interactively explore the whole genome sequence data using several computational methods and charts. ORIS is well suited for origin finding across bacteria, archaea and to some extent eukaryotes. ORIS was used for hypothesizing putative origin of replication sites in *Plasmodium falciparum*, which were later experimentally verified [@agarwal2017identification].


We have tested and validated our tool, ORIS, by performing a case study, available as a supplementary document (https://github.com/urmi-21/ORIS). The method details are also included in the supplementary document. A user guide is available from https://github.com/urmi-21/ORIS.

ORIS is developed particularly for biologists and researchers who are working in the area of DNA replication. ORIS allows users with little or no programming background to interactively explore whole genome sequences and identify the putative origin of replication sites in the genome of interest. An obvious advantage of using ORIS is that all the computational methods implemented in ORIS are accessible through a simple and intuitive GUI but the user has to manually document/log the metadata associated with the results (e.g., the method used, method parameters, and input files). We plan to implement automatic logging in future releases of ORIS so that this information is easily recorded along with the results, which will make it, even more, easier to reproduce the results while minimizing user efforts.


# Acknowledgements
US would like to thank the help and facilities provided by South Asian
University, New Delhi and SCIS-JNU, New Delhi. AK would like to thank SCIS-JNU, New Delhi,
for all the support during the project period.

# References
