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
 - name: Department of Electrical Engineering and Bharti School of Telecommunication Technology and Management, IIT Delhi, India
   index: 2
- name: Special centre for Molecular Medicine, Jawaharlal Nehru University, New Delhi, India
   index: 3
date: 14 July 2019
bibliography: paper.bib
---

# Summary

A remarkable yet very precise event that takes place within the cell, is called DNA replication or chromosome copy [@alberts2013essential].
The way it is done in eukaryotes is vastly different from prokaryotes yet something is common as well. Molecular replication machinery is complex and very less is understood in terms of its structure and function [@krebs2013lewin]. The binding of Origin Recognition Complex protein (ORC) enables recruitment of the recognition complexes such as CDCs and MCMs (mini chromosome maintenance) to carry out the process.
In simple terms, the origin of replication is a small segment in the genome at which the replication process is initiated. 

Bacterial genomes generally have a single origin of DNA replication whereas archea and eukaryotes have multiple replication sites and this organizational structure may be needed to complete the replication task within the single S phase of the cell cycle [@o2013principles; @egan2005microreview]. 
Experimental studies carried out on the model organism *S. cerevisiae* brought out greater insights in understanding the complex replication process. A typical origin sequence in *S. cerevisiae* has a DNA segment called Autonomous Replication Sequence (ARS) and this further has an essential A element and multiple B elements and all these are required for proper origin function or firing. 
This kind of organizational structure may be there in other eukaryotic genomes but they are not very obvious. We will have to exploit this contextual information as part of our computational searches. 
Hence, software modules should be developed incorporating many such useful contextual information for sequence analysis, with the hope that biologists will benefit and it may provide them a helping hand to search for origin like sequences in any genome of their choice. 

There are a few online tools/databases which may help in finding the origin of replication sites for a few species [@frank2000oriloc; @gao2008ori]. In this paper we present a standalone software tool, ORIS, for interactive analysis of prokaryotic genomes in order to find the origin of replication sites in the genome.
An effort is made to develop a software suite that does context based analysis which includes DSP (digital signal processing) based correlation measures, all types of skew measures and entropy based methods for predicting the origin of replication [@beauchamp1979digital; @shah2012nucleotide].
In addition features such as pattern matching, generating sequence logo, finding entropy distribution along the length of the genome, DNA bending profiles and composition statistics are also available as part of analysis. 

ORIS is a novel tool that lets the user interactively explore the whole genome sequence data using a number of computational methods and charts. ORIS is well suited for origin finding across bacteria, archaea and to some extent eukaryotes [@agarwal2017identification].
We have described applicability of our tool, ORIS, in a case study available as a supplementary document (https://github.com/urmi-21/ORIS). The method details could also be found in the supplementary document. A user guide is available from https://github.com/urmi-21/ORIS.

# Acknowledgements
US would like to thank the help and facilities provided by South Asian
University, New Delhi and SCIS-JNU, New Delhi. AK would like to thank SCIS-JNU, New Delhi,
for all the support during the project period.

# References