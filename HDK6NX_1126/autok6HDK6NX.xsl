<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes"/>
  
  <xsl:key name="kVaros" match="auto" use="tulaj/varos"/>
  
  <xsl:template match="/">
    <html><body>
        <h2>Autók városonként</h2>
        
        <table border="1">
          <tr><th>Város</th><th>Darabszám</th></tr>
          
          <xsl:for-each select="autok/auto[generate-id() = generate-id(key('kVaros', tulaj/varos)[1])]">
            <tr>
              <td><xsl:value-of select="tulaj/varos"/></td>
              <td><xsl:value-of select="count(key('kVaros', tulaj/varos))"/></td>
            </tr>
          </xsl:for-each>
          
          <tr>
            <td><b>Összesen</b></td>
            <td><b><xsl:value-of select="count(autok/auto)"/></b></td>
          </tr>
        </table>
        
      </body></html>
  </xsl:template>
  
</xsl:stylesheet>
