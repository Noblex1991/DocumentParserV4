# DocumentParserV4
A java application to turn EffeVi eCommerce's big orders into picking tables document 

Made by Alessandro D'Armiento for T18 EffeVi to create picking dispatch and weighers documents from large wholesale orders.

Reads .pdf documents as input and gives .doc tables as output with products to dispatch and weigh divided by buyer, supplier and registered office.
Tables are then used by weighers and dispatcher to load trucks.

Uses external libraries:
  Apache PDFBox to parse text from PDF files
  Apache POI to create .doc documents
  Apache Commons... because I love it! :D 



ToDo: 
  Forget about PDFs and read everything from DB as soon as I will be allowed

