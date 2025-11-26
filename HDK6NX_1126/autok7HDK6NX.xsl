<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
  
  <xsl:template match="/">
    <autok>
      <xsl:for-each select="autok/auto">
        <xsl:sort select="ar" data-type="number" order="ascending"/>
        <auto rsz="{@rsz}">
          <tipus><xsl:value-of select="tipus"/></tipus>
          <ar><xsl:value-of select="ar"/></ar>
          <szin><xsl:value-of select="szin"/></szin>
          <tulaj>
            <nev><xsl:value-of select="tulaj/nev"/></nev>
            <varos><xsl:value-of select="tulaj/varos"/></varos>
          </tulaj>
        </auto>
      </xsl:for-each>
    </autok>
  </xsl:template>
  
</xsl:stylesheet>
