# DocumentParserV4
A java application to turn EffeVi eCommerce's big orders into picking tables document 

Made by Alessandro D'Armiento for T18 EffeVi to create picking dispatch and weighers documents from large wholesale orders.

Read .pdf documents in input and give .doc tables in output with products to dispatch and weigh divided by buyer, supplier and registered offices.
Tables are then used by weighers and dispatcher to load trucks.

Uses external libraries:
  Apache PDFBox to parse text from PDF files
  Apache POI to create .doc documents
  Apache Commons... because I love it! :D 



ToDo: 
  Forget about PDFs and read everything from DB

