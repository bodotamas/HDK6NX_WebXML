<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
  
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <meta charset="UTF-8"/>
        <title>Órarend – 2025. I. félév.</title>
      </head>
      <body>
        <h1>Órarend – 2025. I. félév.</h1>
        
        <table border="1">
          <tr>
            <th>ID</th>
            <th>Típus</th>
            <th>Tárgy</th>
            <th>Nap</th>
            <th>Tól</th>
            <th>Ig</th>
            <th>Helyszín</th>
            <th>Oktató</th>
            <th>Szak</th>
          </tr>
          
          <xsl:for-each select="HDK6NX_orarend/ora">
            <tr>
              <td><xsl:value-of select="@id"/></td>
              <td><xsl:value-of select="@tipus"/></td>
              <td><xsl:value-of select="targy/@nev"/></td>
              <td><xsl:value-of select="idopont/nap/@napn"/></td>
              <td><xsl:value-of select="idopont/tol/@tolt"/></td>
              <td><xsl:value-of select="idopont/ig/@igt"/></td>
              <td><xsl:value-of select="helyszin/@hol"/></td>
              <td><xsl:value-of select="oktato/@neve"/></td>
              <td><xsl:value-of select="szak/@sznev"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
