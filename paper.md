---
title: 'ORIS: '
tags:
  - Origin of replication
  - Whole genome analysis
  - Bacterial genome
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

A remarkable yet very precise event that takes place within the cell
, is called DNA replication or chromosome copy \cite{BruceA}.
The way it is done in eukaryotes is vastly different from prokaryotes
yet something is common as well. Molecular replication machinery is
complex and very less is understood in terms of its structure and
function \cite{genex}. The binding of Origin Recognition Complex protein
(ORC) enables recruitment of the recognition complexes such as CDCs
and MCMs(mini chromosome maintenance) to carry out the process. In simple terms, the origin of
replication is a small segment in the genome at which the replication process
is initiated. 

Bacterial genomes generally have a single origin of DNA replication
whereas archea and eukaryotes have multiple replication sites and this organizational
structure may be needed to complete the replication task within the
single `S' phase of the cell
cycle \cite{Odonnel,EganES}. Experimental studies carried out on
the model organism \emph{S. cerevisiae} brought out greater insights
in understanding the complex replication process. A typical origin
sequence in \emph{S. cerevisiae} has a DNA segment called `Autonomous
Replication Sequence (ARS)' and this further has
an essential `A' element and
multiple `B' elements and all
these are required for proper origin function or firing. This kind
of organizational structure may be there in other eukaryotic genome
sequences but they are not very obvious. We will have to exploit this contextual information
as part of our computational searches. Hence, software modules should be
developed incorporating many such useful contextual information for
sequence analysis, with the hope that biologists will benefit and
it may provide them a helping hand to search for origin like sequences
in any genome of their choice. 

There are few online software tools which may help
in finding the origin sites \cite{CarolineF,orifinder}, which uses or implements skew measures, random walk and specific motif search.
An effort is made to develop a software suite that does context based analysis which includes DSP (digital signal processing) based correlation measures, all types of skew measures and entropy based methods for predicting
the origin of replication \cite{Beauchamp,chari}. In addition features such as pattern matching,
generating sequence logo, finding entropy distribution along the length
of the genome, DNA bending profiles and composition statistics are also available as part of analysis. To best of our
knowledge our software package is novel in that sense and will be a useful tool
for origin finding across bacteria, archaea and to some extent eukaryotes. Our tool runs on the local machine, thus ensures security as no data has to transmitted over the internet.





# Acknowledgements
US would like to thank the help and facilities provided by South Asian
University, New Delhi and SCIS-JNU, New Delhi. AK would like to thank SCIS-JNU, New Delhi,
for all the support during the project period.

# References
