<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes"/>
  
  <xsl:template match="/">
    <html><body>
        <h2>Miskolci autók rendszámai</h2>
        <ul>
          <xsl:for-each select="autok/auto[tulaj/varos='Miskolc']">
            <li><xsl:value-of select="@rsz"/></li>
          </xsl:for-each>
        </ul>
      </body></html>
  </xsl:template>
</xsl:stylesheet>
