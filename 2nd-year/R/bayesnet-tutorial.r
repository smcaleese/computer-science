# P(hear disease) = ph = the percentage of the population that has heart disease or the probability
# that a person has heart disease given no evidence
ph = 0.3*0.4*0.8 + 0.7*0.6*0.1 + 0.7*0.4*0.5 + 0.3*0.6*0.4

# P(high blood pressure) = pb
pb = 0.35*0.7 + 0.65*0.3

# prob. of heart disease given that they have high blood pressure = P(H|B) = phb
phb = (0.35*0.7)/0.44

# high blood pressure and an abnormal electrocardiogram = p(H and B) pbANDe
pbANDe = 0.35*0.56 + 0.65*0.01

# heart disease given high blood pressure and an abnormal electrocardiogram = p(H|B and E) = phbANDe
phbANDe = (0.35*0.56)/0.2025

# heart disease given high blood pressure and a normal electrocardiogram = p(H|B and ¬E) = phbANDNOTe
bANDNOTe = 0.35*0.7*0.2 + 0.65*0.1*0.9
phbANDNOTe = (0.35*0.7*0.2) / bANDNOTe

# heart disease given smoking = p(H|S) = phs
phs = 0.8*0.4 + 0.4*(1-0.4)

# an abnormal electrocardiogram given smoking = p(E|S) = pes 
pes = 0.56*0.8 + 0.44*0.1

# heart disease given an abnormal electrocardiogram and smoking = p(H|E and S) = pheANDs
# p(H|E and s) = p(H and E and S)/p(E and S) = (p(H|S)*0.8) / p(E|S)
pheANDs = (0.56*0.8)/0.492