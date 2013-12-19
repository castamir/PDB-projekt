# makefile pro preklad LaTeX verze Bc. prace
# (c) 2008 Michal Bidlo
# E-mail: bidlom AT fit vutbr cz
#===========================================
# asi budete chtit prejmenovat:
CO=pdb
CO2=tsql2

all: $(CO).pdf $(CO2).pdf

pdf: $(CO).pdf $(CO2).pdf

$(CO).ps: $(CO).dvi
	dvips $(CO)
    

$(CO).pdf: clean
	pdflatex $(CO)
	bibtex $(CO)
	pdflatex $(CO)
	pdflatex $(CO)

$(CO).dvi: $(CO).tex $(CO).bib
	latex $(CO)
	bibtex $(CO)
	latex $(CO)
	latex $(CO)

$(CO2).ps: $(CO2).dvi
	dvips $(CO2)    
    
$(CO2).pdf: clean
	pdflatex $(CO2)
	bibtex $(CO2)
	pdflatex $(CO2)
	pdflatex $(CO2)
    
$(CO2).dvi: $(CO2).tex $(CO2).bib
	latex $(CO2)
	bibtex $(CO2)
	latex $(CO2)
	latex $(CO2)


clean:
	rm -f *.dvi *.log $(CO).blg $(CO).bbl $(CO).toc *.aux $(CO).out $(CO).lof $(CO2).blg $(CO2).bbl $(CO2).toc *.aux $(CO2).out $(CO2).lof
	rm -f $(CO).pdf $(CO2).pdf
	rm -f *~

pack:
	tar czvf pdb-doku.tar.gz *.tex *.bib *.bst ./fig/* ./cls/* Makefile Changelog
