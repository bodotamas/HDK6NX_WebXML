<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes"/>
  
  <xsl:template match="/">
    <html><body>
        <h2>Autók ára szerint rendezve</h2>
        <table border="1">
          <tr><th>Rendszám</th><th>Ár</th></tr>
          <xsl:for-each select="autok/auto">
            <xsl:sort select="ar" data-type="number" order="ascending"/>
            <tr>
              <td><xsl:value-of select="@rsz"/></td>
              <td><xsl:value-of select="ar"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body></html>
  </xsl:template>
</xsl:stylesheet>
